package com.smoc.cloud.identification.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.identification.entity.IdentificationAccountInfo;
import com.smoc.cloud.identification.entity.IdentificationOrdersInfo;
import com.smoc.cloud.identification.repository.IdentificationAccountInfoRepository;
import com.smoc.cloud.identification.repository.IdentificationOrdersInfoRepository;
import com.smoc.cloud.parameter.entity.ParameterExtendSystemParamValue;
import com.smoc.cloud.parameter.repository.ParameterExtendSystemParamValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IdentificationOrdersInfoService {

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private IdentificationAccountInfoRepository identificationAccountInfoRepository;

    @Resource
    private IdentificationOrdersInfoRepository identificationOrdersInfoRepository;

    @Resource
    private ParameterExtendSystemParamValueRepository parameterExtendSystemParamValueRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<IdentificationOrdersInfoValidator> page(PageParams<IdentificationOrdersInfoValidator> pageParams) {
        return identificationOrdersInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<IdentificationOrdersInfo> data = identificationOrdersInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IdentificationOrdersInfo entity = data.get();
        IdentificationOrdersInfoValidator identificationOrdersInfoValidator = new IdentificationOrdersInfoValidator();
        BeanUtils.copyProperties(entity, identificationOrdersInfoValidator);

        //转换日期
        identificationOrdersInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(identificationOrdersInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param identificationOrdersInfoValidator
     * @param op                                操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IdentificationOrdersInfoValidator identificationOrdersInfoValidator, String op) {

        try {
            Optional<IdentificationAccountInfo> optional = identificationAccountInfoRepository.findById(identificationOrdersInfoValidator.getIdentificationAccount());

            if(!optional.isPresent()){
                return ResponseDataUtil.buildError(ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
            }

            //如果是测试用户 不保存订单
            if("2".equals(optional.get().getAccountType())){
                return ResponseDataUtil.buildSuccess("0001","测试已调通，可申请正式账户");
            }
            //读取价格
            List<ParameterExtendSystemParamValue> costPrice = parameterExtendSystemParamValueRepository.findParameterExtendSystemParamValueByBusinessId("IDENTITY_COST");
            Map<String, BigDecimal> map = costPrice.stream().collect(Collectors.toMap(p -> p.getParamKey(), p -> new BigDecimal(p.getParamValue())));
            log.info("[身份认证成本价][{}]数据:{}", op, JSON.toJSONString(map));
            if ("NAME".equals(identificationOrdersInfoValidator.getOrderType())) {
                identificationOrdersInfoValidator.setIdentificationPrice(optional.get().getIdentificationPrice());
                identificationOrdersInfoValidator.setCostPrice(map.get("IDENTITY_NAME_PRICE"));
            } else {
                identificationOrdersInfoValidator.setIdentificationPrice(optional.get().getIdentificationFacePrice());
                identificationOrdersInfoValidator.setCostPrice(map.get("IDENTITY_FACE_PRICE"));
            }

            log.info("[1身份认证订单][{}]数据:{}", op, JSON.toJSONString(identificationOrdersInfoValidator));
            //判断余额是否充足
            boolean able = financeAccountRepository.checkAccountUsableSum(identificationOrdersInfoValidator.getIdentificationAccount(), identificationOrdersInfoValidator.getIdentificationPrice());
            if (!able) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_ABLE_ERROR.getCode(), ResponseCode.PARAM_ABLE_ERROR.getMessage());
            }
            log.info("[2身份认证订单][{}]数据:{}", op, JSON.toJSONString(identificationOrdersInfoValidator));
            //冻结金额
            financeAccountRepository.freeze(identificationOrdersInfoValidator.getIdentificationAccount(), identificationOrdersInfoValidator.getIdentificationPrice());
            log.info("[3身份认证订单][{}]数据:{}", op, JSON.toJSONString(identificationOrdersInfoValidator));
            //生成订单
            IdentificationOrdersInfo entity = new IdentificationOrdersInfo();
            BeanUtils.copyProperties(identificationOrdersInfoValidator, entity);
            //转换日期格式
            entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(identificationOrdersInfoValidator.getCreatedTime()));

            //记录日志
            log.info("[4身份认证订单][{}]数据:{}", op, JSON.toJSONString(entity));
            identificationOrdersInfoRepository.saveAndFlush(entity);

            //如果是测试用户
            if("2".equals(optional.get().getAccountType())){
                return ResponseDataUtil.buildSuccess("0001","测试已调通，可申请正式账户",entity.getOrderNo());
            }
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 计费
     *
     * @param identificationOrdersInfoValidator
     * @return
     */
    @Transactional
    public ResponseData update(IdentificationOrdersInfoValidator identificationOrdersInfoValidator) {
        try {
            Optional<IdentificationOrdersInfo> optional = identificationOrdersInfoRepository.findIdentificationOrdersInfoByOrderNo(identificationOrdersInfoValidator.getOrderNo());

            IdentificationOrdersInfo entity = optional.get();

            //订单请求失败，解冻金额 不扣费
            if ("00".equals(identificationOrdersInfoValidator.getIdentificationPriceStatus())) {
                financeAccountRepository.unfreezeFree(entity.getIdentificationAccount(), entity.getIdentificationPrice());
            }

            //订单请求成功，解冻金额 扣费
            if ("02".equals(identificationOrdersInfoValidator.getIdentificationPriceStatus())) {
                financeAccountRepository.unfreeze(entity.getIdentificationAccount(), entity.getIdentificationPrice());
            }

            //修改订单
            entity.setIdentificationStatus(identificationOrdersInfoValidator.getIdentificationStatus());
            entity.setIdentificationPriceStatus(identificationOrdersInfoValidator.getIdentificationPriceStatus());
            entity.setIdentificationOrderNo(identificationOrdersInfoValidator.getIdentificationOrderNo());
            entity.setIdentificationMessage(identificationOrdersInfoValidator.getIdentificationMessage());
            entity.setUpdatedBy("系统");
            entity.setUpdatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

            identificationOrdersInfoRepository.saveAndFlush(entity);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), e.getMessage());
        }


    }
}
