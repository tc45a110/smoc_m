package com.smoc.cloud.customer.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountSignRegister;
import com.smoc.cloud.customer.repository.AccountSignRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class AccountSignRegisterService {

    @Resource
    private AccountSignRegisterRepository accountSignRegisterRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterValidator>> page(PageParams<AccountSignRegisterValidator> pageParams){
        PageList<AccountSignRegisterValidator> pageList = this.accountSignRegisterRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(pageList);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<AccountSignRegisterValidator> findById(String id) {
        Optional<AccountSignRegister> data = accountSignRegisterRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountSignRegister entity = data.get();
        AccountSignRegisterValidator accountSignRegisterValidator = new AccountSignRegisterValidator();
        BeanUtils.copyProperties(entity, accountSignRegisterValidator);

        //转换日期
        accountSignRegisterValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountSignRegisterValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountSignRegisterValidator
     * @param op     操作类型 为 add、edit
     * @return
     */
    @Transactional
    public ResponseData save(AccountSignRegisterValidator accountSignRegisterValidator, String op) {

        //转BaseUser存放对象
        AccountSignRegister entity = new AccountSignRegister();
        BeanUtils.copyProperties(accountSignRegisterValidator, entity);

//        List<EnterpriseSignCertify> data = enterpriseSignCertifyRepository.findByGroupIdAndMobileAndStatus(entity.getGroupId(),entity.getMobile(), "1");
//
//        //add查重
//        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
//            return ResponseDataUtil.buildError("组里已经存在此手机号码，请修改");
//        }
//        //edit查重orgName
//        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
//            boolean status = false;
//            Iterator iter = data.iterator();
//            while (iter.hasNext()) {
//                EnterpriseBookInfo organization = (EnterpriseBookInfo) iter.next();
//                if (!entity.getId().equals(organization.getId()) ) {
//                    status = true;
//                    break;
//                }
//            }
//            if (status) {
//                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
//            }
//        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[签名报备][{}]数据:{}",op,JSON.toJSONString(entity));

        accountSignRegisterRepository.saveAndFlush(entity);
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

        AccountSignRegister data = accountSignRegisterRepository.findById(id).get();

        //记录日志
        log.info("[签名报备][delete]数据:{}", JSON.toJSONString(data));
        accountSignRegisterRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }
}
