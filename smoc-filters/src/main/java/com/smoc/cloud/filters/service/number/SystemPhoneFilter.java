package com.smoc.cloud.filters.service.number;

import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * @param filtersService  业务服务
     * @param isBlackListType 黑名单过滤类别
     * @param phone           手机号
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object isBlackListType, String phone) {

        Map<String, String> result = new HashMap<>();
        if (null == isBlackListType || StringUtils.isEmpty(isBlackListType.toString())) {
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

            Boolean isExistBlackList = filtersService.systemNumberBlackListFilter(phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //系统黑名单
            if (isExistBlackList) {
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
            Boolean isExistBlackList = filtersService.systemNumberBlackListFilter(phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //系统黑名单
            if (isExistBlackList) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getMessage());
                return result;
            }
            //本地黑名单
            if (filtersService.localNumberBlackListFilter(phone)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_LOCAL_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_LOCAL_NUMBER_BLACK_LIST.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //中(系统+本地+三方)
        if ("HIGH".equals(isBlackListType.toString())) {
            Boolean isExistBlackList = filtersService.systemNumberBlackListFilter(phone);//FilterInitialize.numberSystemBlackFilter.isContain(phone,1);
            //系统黑名单
            if (isExistBlackList) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_SYSTEM_NUMBER_BLACK_LIST.getMessage());
                return result;
            }
            //本地黑名单
            if (filtersService.localNumberBlackListFilter(phone)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_LOCAL_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_LOCAL_NUMBER_BLACK_LIST.getMessage());
                return result;
            }
            //第三方黑名单
            if (filtersService.thirdNumberBlackListFilter(phone)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.IS_EXIST_THIRD_NUMBER_BLACK_LIST.getCode());
                result.put("message", FilterResponseCode.IS_EXIST_THIRD_NUMBER_BLACK_LIST.getMessage());
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


}
