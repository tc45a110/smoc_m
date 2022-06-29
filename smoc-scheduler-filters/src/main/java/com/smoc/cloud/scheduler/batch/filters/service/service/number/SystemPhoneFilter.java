package com.smoc.cloud.scheduler.service.filters.service.number;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.filters.utils.RedisFilterConstant;
import com.smoc.cloud.scheduler.service.filters.service.FiltersService;
import com.smoc.cloud.scheduler.batch.filters.service.utils.FilterResponseCode;
import com.smoc.cloud.scheduler.tools.redis.RedisModuleBloomFilter;
import com.smoc.cloud.scheduler.tools.redis.RedisModuleCuckooFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统手机号黑名单过滤 包括了本地白名单洗白操作
 */
@Slf4j
@Component
public class SystemPhoneFilter {

    @Autowired
    private RedisModuleBloomFilter redisModuleBloomFilter;

    @Autowired
    private RedisModuleCuckooFilter redisModuleCuckooFilter;

    /**
     * @param filtersService          业务服务
     * @param isBlackListType         黑名单过滤类别
     * @param account                 业务账号
     * @param phone                   手机号
     * @param isIndustryBlackListType 行业黑名单行业
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object isBlackListType, String account, String phone, Object isIndustryBlackListType) {

        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(isBlackListType)) {
            result.put("result", "false");
            return result;
        }
        //黑名单过滤层级
        //log.info("[号码_黑名单_层级]:{}", isBlackListType);
        //不过滤
        if ("NO".equals(isBlackListType.toString())) {
            result.put("result", "false");
            return result;
        }
        //低(系统黑名单)
        if ("LOW".equals(isBlackListType.toString())) {
            //log.info("[号码_mobile]:{}", phone);
            Boolean isExistBlackList = redisModuleBloomFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT, phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //log.info("[号码_isExistBlackList]:{}", isExistBlackList);
            //系统黑名单
            if (isExistBlackList) {

                //业务账号白名单
                Boolean isExistAccountWhiteList = filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER_WHITE + account, phone);
                if (isExistAccountWhiteList) {
                    result.put("result", "false");
                    return result;
                }
                //业务账号配置洗白操作
                Boolean accountWhite = this.accountWhiteRegular(filtersService, account, phone);
                if (accountWhite) {
                    result.put("result", "false");
                    return result;
                }
                //行业白名单洗白
//                if (industryWhite(filtersService, isIndustryBlackListType, phone)) {
//                    result.put("result", "false");
//                    return result;
//                }
                //系统白名单
                Boolean systemWhiteList = redisModuleCuckooFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_WHITE, phone);
                if (systemWhiteList) {
                    result.put("result", "false");
                    return result;
                }
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //中(系统+本地)
        if ("MIDDLE".equals(isBlackListType.toString())) {
            Boolean isExistBlackList = redisModuleBloomFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT, phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //系统黑名单
            if (isExistBlackList) {

                //业务账号白名单
                Boolean isExistAccountWhiteList = filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER_WHITE + account, phone);
                if (isExistAccountWhiteList) {
                    result.put("result", "false");
                    return result;
                }

                //业务账号配置洗白操作
                Boolean accountWhite = this.accountWhiteRegular(filtersService, account, phone);
                if (accountWhite) {
                    result.put("result", "false");
                    return result;
                }
                //行业白名单洗白
//                if (industryWhite(filtersService, isIndustryBlackListType, phone)) {
//                    result.put("result", "false");
//                    return result;
//                }
                Boolean systemWhiteList = redisModuleCuckooFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_WHITE, phone);
                if (systemWhiteList) {
                    result.put("result", "false");
                    return result;
                }
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getMessage());
                return result;
            }

            result.put("result", "false");
            return result;
        }

        //中(系统+本地+三方)
        if ("HIGH".equals(isBlackListType.toString())) {
            Boolean isExistBlackList = redisModuleBloomFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT, phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //系统黑名单
            if (isExistBlackList) {

                //业务账号白名单
                Boolean isExistAccountWhiteList = filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER_WHITE + account, phone);
                if (isExistAccountWhiteList) {
                    result.put("result", "false");
                    return result;
                }
                //业务账号配置洗白操作
                Boolean accountWhite = this.accountWhiteRegular(filtersService, account, phone);
                if (accountWhite) {
                    result.put("result", "false");
                    return result;
                }
                //行业白名单洗白
//                if (industryWhite(filtersService, isIndustryBlackListType, phone)) {
//                    result.put("result", "false");
//                    return result;
//                }
                Boolean systemWhiteList = redisModuleCuckooFilter.isExist(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_WHITE, phone);
                if (systemWhiteList) {
                    result.put("result", "false");
                    return result;
                }
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getMessage());
                return result;
            }

            result.put("result", "false");
            return result;
        }

        result.put("result", "true");
        result.put("code", FilterResponseCode.NO_CONFIG_NUMBER_BLACK_LIST.getCode());
        result.put("message", FilterResponseCode.NO_CONFIG_NUMBER_BLACK_LIST.getMessage());
        return result;

    }

    /**
     * 业务账号配置的白名单规则
     *
     * @param filtersService
     * @param account
     * @param phone
     * @return 如果洗白，则返回true
     */
    public Boolean accountWhiteRegular(FiltersService filtersService, String account, String phone) {
        Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" + account);
        if (!StringUtils.isEmpty(whitePatten)) {
            //log.info("[号码_白名单_扩展参数]");
            Boolean validator = filtersService.validator(whitePatten.toString(), phone);
            if (validator) {
                return validator;
            }
        }
        Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "regular:" + account);
        if (!StringUtils.isEmpty(regularPatten)) {
            //log.info("[号码_正则_扩展参数]");
            if (filtersService.validator(regularPatten.toString(), phone)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 行业白名单洗白
     *
     * @param filtersService
     * @param industry
     * @param phone
     * @return 如果洗白，则返回true
     */
    public Boolean industryWhite(FiltersService filtersService, Object industry, String phone) {
        if (StringUtils.isEmpty(industry)) {
            return false;
        }
        Boolean isMember = filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_SYSTEM_INDUSTRY_WHITE + industry, phone);
        return isMember;
    }


}
