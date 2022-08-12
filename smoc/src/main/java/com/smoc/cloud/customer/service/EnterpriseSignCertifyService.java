package com.smoc.cloud.customer.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseSignCertify;
import com.smoc.cloud.customer.repository.EnterpriseSignCertifyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class EnterpriseSignCertifyService {

    @Resource
    private EnterpriseSignCertifyRepository enterpriseSignCertifyRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseSignCertifyValidator>> page(PageParams<EnterpriseSignCertifyValidator> pageParams){
        PageList<EnterpriseSignCertifyValidator> pageList = this.enterpriseSignCertifyRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(pageList);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<EnterpriseSignCertifyValidator> findById(String id) {
        Optional<EnterpriseSignCertify> data = enterpriseSignCertifyRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseSignCertify entity = data.get();
        EnterpriseSignCertifyValidator enterpriseSignCertifyValidator = new EnterpriseSignCertifyValidator();
        BeanUtils.copyProperties(entity, enterpriseSignCertifyValidator);

        //转换日期
        enterpriseSignCertifyValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(enterpriseSignCertifyValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseSignCertifyValidator
     * @param op     操作类型 为 add、edit
     * @return
     */
    @Transactional
    public ResponseData save(EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, String op) {

        //转BaseUser存放对象
        EnterpriseSignCertify entity = new EnterpriseSignCertify();
        BeanUtils.copyProperties(enterpriseSignCertifyValidator, entity);

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseSignCertifyValidator.getCreatedTime()));
        //记录日志
        log.info("[企业签名资质管理][{}]数据:{}",op,JSON.toJSONString(entity));

        enterpriseSignCertifyRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        EnterpriseSignCertify data = enterpriseSignCertifyRepository.findById(id).get();

        //记录日志
        log.info("[企业签名资质管理][delete]数据:{}", JSON.toJSONString(data));
        enterpriseSignCertifyRepository.delete(id,"0");

        return ResponseDataUtil.buildSuccess();
    }
}
