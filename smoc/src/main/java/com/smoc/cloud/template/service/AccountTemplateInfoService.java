package com.smoc.cloud.template.service;


import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.template.entity.AccountTemplateContent;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.tools.message.RocketProducerFilterMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 模板管理
 */
@Slf4j
@Service
public class AccountTemplateInfoService {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RocketProducerFilterMessage rocketProducerFilterMessage;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(PageParams<AccountTemplateInfoValidator> pageParams) {
        PageList<AccountTemplateInfoValidator> data = accountTemplateInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据id 查询
     *
     * @param templateId
     * @return
     */
    public ResponseData<AccountTemplateInfoValidator> findById(String templateId) {

        Optional<AccountTemplateInfo> entityOptional = accountTemplateInfoRepository.findById(templateId);
        if (!entityOptional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountTemplateInfo entity = entityOptional.get();
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        BeanUtils.copyProperties(entity, accountTemplateInfoValidator);
        if (null != entity.getCheckDate()) {
            accountTemplateInfoValidator.setCheckDate(DateTimeUtils.getDateTimeFormat(entity.getCheckDate()));
        }
        accountTemplateInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountTemplateInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountTemplateInfoValidator
     * @param op                           操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountTemplateInfoValidator> save(AccountTemplateInfoValidator accountTemplateInfoValidator, String op) {

        AccountTemplateInfo entity = new AccountTemplateInfo();
        BeanUtils.copyProperties(accountTemplateInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountTemplateInfoValidator.getCreatedTime()));
        if (null != accountTemplateInfoValidator.getCheckDate())
            entity.setCheckDate(DateTimeUtils.getDateTimeFormat(accountTemplateInfoValidator.getCheckDate()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[模板管理][模板信息][{}]数据:{}", op, JSON.toJSONString(entity));
        entity.setTemplateContent(entity.getTemplateContent().trim());
        accountTemplateInfoRepository.saveAndFlush(entity);


        return ResponseDataUtil.buildSuccess(accountTemplateInfoValidator);
    }

    /**
     * 注销模板
     *
     * @param templateId
     * @return
     */
    @Transactional
    public ResponseData cancelTemplate(String templateId, String templateStatus) {
        accountTemplateInfoRepository.cancelTemplate(templateId, templateStatus);
        return ResponseDataUtil.buildSuccess();
    }

    @Transactional
    public ResponseData deleteById(String id) {

        AccountTemplateInfo data = accountTemplateInfoRepository.findById(id).get();
        //记录日志
        log.info("[模板管理][delete]数据:{}", JSON.toJSONString(data));
        accountTemplateInfoRepository.deleteById(data.getTemplateId());

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 加载业务账号固定模版到账号  包括CMPP\HTTP模版
     *
     * @param account
     */
    @Async
    public void loadFixedTemplate(String account) {
        List<AccountTemplateContent> fixedTemplate = this.accountTemplateInfoRepository.findFixedTemplate(account);
        if (null == fixedTemplate || fixedTemplate.size() < 1) {
            redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + account);
            this.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + "list", account);
            //发送广播通知
            this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
            return;
        }
        String templateContent = "";
        for (AccountTemplateContent template : fixedTemplate) {
            if (StringUtils.isEmpty(templateContent)) {
                templateContent = template.getContent();
            } else {
                templateContent = templateContent + "|" + template.getContent();
            }
        }
        redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + account, templateContent);
        redisTemplate.opsForSet().add(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + "list", account);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
    }

    /**
     * 加载CMPP中变量模版，但是后续不过滤
     *
     * @param account
     */
    @Async
    public void loadNoFilterVariableTemplate(String account) {
        List<AccountTemplateContent> noFilterVariableTemplate = this.accountTemplateInfoRepository.findNoFilterVariableTemplate(account);
        if (null == noFilterVariableTemplate || noFilterVariableTemplate.size() < 1) {
            redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:" + account);
            this.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:list", account);
            //发送广播通知
            this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
            return;
        }
        String templateContent = "";
        for (AccountTemplateContent template : noFilterVariableTemplate) {
            if (StringUtils.isEmpty(templateContent)) {
                templateContent = template.getContent();
            } else {
                templateContent = templateContent + "|" + template.getContent();
            }
        }
        //替换占位符
        Map<String, String> paramMap = new HashMap<>();
        for (int j = 1; j < 11; j++) {
            paramMap.put(j + "", ".*");
        }
        StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
        String messageContent = strSubstitutor.replace(templateContent);
        redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:" + account, messageContent);
        redisTemplate.opsForSet().add(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:list", account);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
    }

    /**
     * 加载CMPP中变量模版，但是后续不过滤
     *
     * @param account
     */
    @Async
    public void loadFilterVariableTemplate(String account) {
        List<AccountTemplateContent> filterVariableTemplate = this.accountTemplateInfoRepository.findCMPPVariableTemplate(account);
        if (null == filterVariableTemplate || filterVariableTemplate.size() < 1) {
            redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:" + account);
            this.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:list", account);
            //发送广播通知
            this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
            return;
        }
        String templateContent = "";
        for (AccountTemplateContent template : filterVariableTemplate) {
            if (StringUtils.isEmpty(templateContent)) {
                templateContent = template.getContent();
            } else {
                templateContent = templateContent + "|" + template.getContent();
            }
        }
        //替换占位符
        Map<String, String> paramMap = new HashMap<>();
        for (int j = 1; j < 11; j++) {
            paramMap.put(j + "", ".*");
        }
        StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
        String messageContent = strSubstitutor.replace(templateContent);
        redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:" + account, messageContent);
        redisTemplate.opsForSet().add(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:list", account);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
    }

    /**
     * 加载CMPP中签名模版
     *
     * @param account
     */
    @Async
    public void loadCMPPSignTemplate(String account) {
        List<AccountTemplateContent> cMPPSignTemplate = this.accountTemplateInfoRepository.findCMPPSignTemplate(account);
        if (null == cMPPSignTemplate || cMPPSignTemplate.size() < 1) {
            redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + account);
            this.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + "list", account);
            //发送广播通知
            this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
            return;
        }
        String templateContent = "";
        for (AccountTemplateContent template : cMPPSignTemplate) {
            if (StringUtils.isEmpty(templateContent)) {
                templateContent = template.getContent();
            } else {
                templateContent = templateContent + "|" + template.getContent();
            }
        }

        redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + account, templateContent);
        redisTemplate.opsForSet().add(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + "list", account);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TEMPLATE);
    }

    /**
     * 根据审核过程来进行加载
     * 加载HTTP、WEB固定模版
     * 现在重新加载所有的模版，添加审核的时候，可以根据模版id 来进行添加和删除
     *
     * @param
     */
    @Async
    public void loadHttpWebTemplate() {
        Map<String, String> httpFixedTemplate = this.accountTemplateInfoRepository.findHttpFixedTemplate();
        redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_FIXED);
        if (null == httpFixedTemplate || httpFixedTemplate.size() < 1) {
            return;
        }
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_FIXED, httpFixedTemplate);
    }

    /**
     * 根据审核过程来进行加载
     * 加载HTTP、WEB变量模版
     * 现在重新加载所有的模版，添加审核的时候，可以根据模版id 来进行添加和删除
     *
     * @param
     */
    @Async
    public void loadHttpWebVariableTemplates() {
        Map<String, String> httpVariableTemplates = this.accountTemplateInfoRepository.findHttpVariableTemplate();
        redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_VARIABLE);
        if (null == httpVariableTemplates || httpVariableTemplates.size() < 1) {
            return;
        }
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_VARIABLE, httpVariableTemplates);
    }


    public void multiSaveHash(String redisKey, Map<String, String> map) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            map.forEach((key, value) -> {
                connection.hSet(RedisSerializer.string().serialize(redisKey),
                        RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(value)));
            });
            connection.close();
            return null;
        });
    }

    /**
     * 从Set中删除
     *
     * @param redisKey
     * @param keyWord
     */
    public void delFromSet(String redisKey, String keyWord) {
        redisTemplate.opsForSet().remove(redisKey, keyWord);
    }

    /**
     * 从Hash中删除
     *
     * @param redisKey
     * @param keyWord
     */
    public void delFromHash(String redisKey, String keyWord) {
        redisTemplate.opsForHash().delete(redisKey, keyWord);
    }

}
