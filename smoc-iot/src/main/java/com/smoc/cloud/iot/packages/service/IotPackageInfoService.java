package com.smoc.cloud.iot.packages.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.iot.validator.PackageCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.packages.entity.IotPackageInfo;
import com.smoc.cloud.iot.packages.repository.IotPackageCardRepository;
import com.smoc.cloud.iot.packages.repository.IotPackageInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IotPackageInfoService {

    @Resource
    private IotPackageInfoRepository iotPackageInfoRepository;
    @Resource
    private IotPackageCardRepository iotPackageCardRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotPackageInfoValidator>> page(PageParams<IotPackageInfoValidator> pageParams) {
        PageList<IotPackageInfoValidator> page = iotPackageInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据套餐id查询，套餐配置的信息，及未配置的 物联网卡信息
     *
     * @param packageId
     * @return
     */
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> list(String packageId,String packageType) {
        List<IotFlowCardsPrimaryInfoValidator> list = iotPackageCardRepository.list(packageId,packageType);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotPackageInfoValidator> findById(String id) {
        Optional<IotPackageInfo> data = iotPackageInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotPackageInfoValidator validator = new IotPackageInfoValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        return ResponseDataUtil.buildSuccess(validator);
    }

    /**
     * 保存或修改
     *
     * @param validator
     * @param op        操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IotPackageInfoValidator validator, String op) {

        Iterable<IotPackageInfo> data = iotPackageInfoRepository.findByPackageName(validator.getPackageName());

        IotPackageInfo entity = new IotPackageInfo();
        BeanUtils.copyProperties(validator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                IotPackageInfo info = (IotPackageInfo) iterator.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(validator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[套餐管理][{}]数据:{}", op, JSON.toJSONString(entity));
        iotPackageInfoRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 保存或修改
     *
     * @param packageCards
     * @return
     */
    @Transactional
    public ResponseData saveConfig(PackageCards packageCards) {

        log.info("[套餐物联网卡配置]:{}", new Gson().toJson(packageCards));
        iotPackageCardRepository.insertPackageCards(packageCards.getPackageId(), packageCards.getCardsId());
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 禁用
     *
     * @param id
     */
    public ResponseData forbidden(String id) {
        this.iotPackageInfoRepository.forbidden(id, "0");
        return ResponseDataUtil.buildSuccess();
    }
}
