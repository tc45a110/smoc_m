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
import com.smoc.cloud.sign.service.SignRegisterService;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountSignRegisterService {

    @Resource
    private AccountSignRegisterRepository accountSignRegisterRepository;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Autowired
    private AccountTemplateInfoService accountTemplateInfoService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterValidator>> page(PageParams<AccountSignRegisterValidator> pageParams) {
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
     * @param op                           操作类型 为 add、edit
     * @return
     */
    @Transactional
    public ResponseData save(AccountSignRegisterValidator accountSignRegisterValidator, String op) {

        //转BaseUser存放对象
        AccountSignRegister entity = new AccountSignRegister();
        BeanUtils.copyProperties(accountSignRegisterValidator, entity);

        //查重
        List<AccountSignRegister> data = accountSignRegisterRepository.findByAccountAndSignExtendNumber(entity.getAccount(), entity.getSignExtendNumber());
        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("账号已存在该签名扩展号");
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                AccountSignRegister organization = (AccountSignRegister) iterator.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountSignRegisterValidator.getCreatedTime()));

        /**
         * 根据账号，处理签名模板
         */
        List<AccountTemplateInfo> templateInfos = this.accountTemplateInfoRepository.findByBusinessAccountAndTemplateClassify(entity.getAccount(), "3");
        //如果不存在，则创建签名模板
        if (null == templateInfos || templateInfos.size() < 1) {
            accountTemplateInfoRepository.createTemplate(accountSignRegisterValidator.getAccount(), accountSignRegisterValidator.getSign());
        } else {
            //如果存在，则修改签名模板内容
            AccountTemplateInfo accountTemplateInfo = templateInfos.get(0);
            String templateContent = accountTemplateInfo.getTemplateContent();
            if (op.equals("add")) {
                templateContent = templateContent + "|" + entity.getSign();
            }
            if (op.equals("edit")) {

                Optional<AccountSignRegister> accountSignRegister = accountSignRegisterRepository.findById(entity.getId());
                if(templateContent.indexOf(accountSignRegister.get().getSign()) ==-1){
                    templateContent = templateContent + "|" + entity.getSign();
                }else{
                    templateContent = templateContent.replace(accountSignRegister.get().getSign(), entity.getSign());
                }

            }
            accountTemplateInfo.setTemplateContent(templateContent);
            accountTemplateInfoRepository.saveAndFlush(accountTemplateInfo);
        }

        accountSignRegisterRepository.saveAndFlush(entity);
        //记录日志
        log.info("[签名报备][{}]数据:{}", op, JSON.toJSONString(entity));

        //更新redis
        this.accountTemplateInfoService.loadCMPPSignTemplate(entity.getAccount());

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据业务账号，查询已占用的签名自定义扩展号
     *
     * @param account
     * @param id      当id 不为空时候，不查询本id的签名自定义扩展号
     * @return
     */
    public ResponseData<List<String>> findExtendDataByAccount(String account, String id) {
        List<String> list = this.accountSignRegisterRepository.findExtendDataByAccount(account, id);
        return ResponseDataUtil.buildSuccess(list);
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
        accountSignRegisterRepository.delete(id, "0");

        return ResponseDataUtil.buildSuccess();
    }

}
