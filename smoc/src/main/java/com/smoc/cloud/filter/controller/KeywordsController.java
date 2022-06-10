package com.smoc.cloud.filter.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.entity.FilterKeyWordsInfo;
import com.smoc.cloud.filter.service.KeywordsService;
import com.smoc.cloud.tools.message.RocketProducerFilterMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 关键词管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/keywords/system")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class KeywordsController {

    @Autowired
    private RocketProducerFilterMessage rocketProducerFilterMessage;

    @Autowired
    private KeywordsService keywordsService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<FilterKeyWordsInfoValidator> page(@RequestBody PageParams<FilterKeyWordsInfoValidator> pageParams) {

        return keywordsService.page(pageParams);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {


        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return keywordsService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterKeyWordsInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterKeyWordsInfoValidator));
        }
        ResponseData responseData = keywordsService.save(filterKeyWordsInfoValidator, op);
        log.info("[关键词]{}", new Gson().toJson(filterKeyWordsInfoValidator));

        //更新关键词到过滤器
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            //系统敏感词
            if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadSystemSensitiveWords();
            }
            //系统审核词
            if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadSystemCheckWords();
            }
            //系统洗敏白词
            if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadSystemWhiteSensitiveWords();
            }
            //系统免审白词
            if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadSystemWhiteCheckWords();
            }
            //行业敏感词
            if ("INFO_TYPE".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadIndustrySensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
            }
            //业务账号敏感词
            if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadAccountSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
            }
            //业务账号审核词
            if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadAccountCheckWords(filterKeyWordsInfoValidator.getBusinessId());
            }
            //业务账号洗敏白词
            if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadAccountWhiteSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
            }
            //业务账号免审白词
            if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadAccountWhiteCheckWords(filterKeyWordsInfoValidator.getBusinessId());
            }
            //通道敏感词
            if ("CHANNEL".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
                this.keywordsService.loadChannelSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
            }
        }
        return responseData;
    }


    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }
        ResponseData<FilterKeyWordsInfo> entity = keywordsService.findById(id);
        ResponseData responseData = keywordsService.deleteById(id);
        //删除关键词从过滤器
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            //删除系统敏感词
            if ("SYSTEM".equals(entity.getData().getKeyWordsBusinessType()) && "BLACK".equals(entity.getData().getBusinessId()) && "BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE, entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_SYSTEM_SENSITIVE);
            }
            //删除系统审核词
            if ("SYSTEM".equals(entity.getData().getKeyWordsBusinessType()) && "CHECK".equals(entity.getData().getBusinessId()) && "CHECK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK, entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_SYSTEM_CHECK);
            }
            //删除系统洗敏白词
            if ("SYSTEM".equals(entity.getData().getKeyWordsBusinessType()) && "WHITE".equals(entity.getData().getBusinessId()) && "WHITE_AVOID_BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromHash(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE, entity.getData().getWaskKeyWords());
            }
            //删除系统免审白词
            if ("SYSTEM".equals(entity.getData().getKeyWordsBusinessType()) && "WHITE".equals(entity.getData().getBusinessId()) && "WHITE_AVOID_CHECK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromHash(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK, entity.getData().getWaskKeyWords());
            }
            //行业敏感词
            if ("INFO_TYPE".equals(entity.getData().getKeyWordsBusinessType()) && "BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE + entity.getData().getBusinessId(), entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TYPE_INFO_SENSITIVE);
            }
            //业务账号敏感词
            if ("BUSINESS_ACCOUNT".equals(entity.getData().getKeyWordsBusinessType()) && "BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE + entity.getData().getBusinessId(), entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_ACCOUNT_SENSITIVE);
            }
            //业务账号审核词
            if ("BUSINESS_ACCOUNT".equals(entity.getData().getKeyWordsBusinessType()) && "CHECK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK + entity.getData().getBusinessId(), entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_ACCOUNT_CHECK);
            }
            //业务账号洗敏白词
            if ("BUSINESS_ACCOUNT".equals(entity.getData().getKeyWordsBusinessType()) && "WHITE_AVOID_BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE + entity.getData().getBusinessId(), entity.getData().getWaskKeyWords());
            }
            //业务账号免审白词
            if ("BUSINESS_ACCOUNT".equals(entity.getData().getKeyWordsBusinessType()) && "WHITE_AVOID_CHECK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK + entity.getData().getBusinessId(), entity.getData().getWaskKeyWords());
            }
            //通道敏感词
            if ("CHANNEL".equals(entity.getData().getKeyWordsBusinessType()) && "BLACK".equals(entity.getData().getKeyWordsType())) {
                this.keywordsService.delFromSet(RedisConstant.FILTERS_CONFIG_CHANNEL_SENSITIVE+ entity.getData().getBusinessId(),entity.getData().getKeyWords());
                //发送广播通知
                this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_CHANNEL_SENSITIVE);
            }
        }
        return responseData;
    }

    /**
     * 批量保存
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @RequestMapping(value = "/bathSave/{op}", method = RequestMethod.POST)
    public void bathSave(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op) {

        keywordsService.bathSave(filterKeyWordsInfoValidator);
    }

    /**
     * 关键字导入
     *
     * @param filterKeyWordsInfoValidator
     */
    @RequestMapping(value = "/expBatchSave", method = RequestMethod.POST)
    public void expBatchSave(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {

        keywordsService.expBatchSave(filterKeyWordsInfoValidator);
    }
}
