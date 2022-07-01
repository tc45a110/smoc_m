package com.smoc.cloud.scheduler.filters.service.service.number;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.filters.service.service.FiltersService;
import com.smoc.cloud.scheduler.filters.service.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号行业黑名单过滤、行业白名单洗白、业务账号手机号洗白规则洗白
 */
@Slf4j
@Component
public class IndustryBlackListFilter {

    /**
     * @param filtersService          业务服务
     * @param isIndustryBlackListType 行业黑名单行业
     * @param account                 业务账号
     * @param phone                   手机号
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object isIndustryBlackListType, String account, String phone) {

        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(isIndustryBlackListType)) {
            result.put("result", "false");
            return result;
        }

        //log.info("[isIndustryBlackListType]:{}",isIndustryBlackListType);
        //log.info("[phone]:{}",phone);
        Boolean isExistIndustryBlackList= filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_SYSTEM_INDUSTRY_BLACK+isIndustryBlackListType,phone);
        //log.info("[isExistIndustryBlackList]:{}",isExistIndustryBlackList);
        if(isExistIndustryBlackList){
            Boolean isMember = filtersService.isSetMember(RedisConstant.FILTERS_CONFIG_SYSTEM_INDUSTRY_WHITE+isIndustryBlackListType,phone);
            //log.info("[isMember]:{}",isMember);
            //行业白名单洗白
            if(isMember){
                result.put("result", "false");
                return result;
            }

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
            result.put("result", "true");
            result.put("code", FilterResponseCode.NUMBER_INDUSTRY_BLACK_FILTER.getCode());
            result.put("message", FilterResponseCode.NUMBER_INDUSTRY_BLACK_FILTER.getMessage());
            return result;
        }

        result.put("result", "false");
        return result;
    }

    /**
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

}
