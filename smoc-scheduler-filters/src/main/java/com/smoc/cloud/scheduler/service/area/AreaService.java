package com.smoc.cloud.scheduler.service.area;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.service.redis.FiltersRedisDataService;
import com.smoc.cloud.scheduler.tools.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 手机号业务区域业务逻辑
 */
@Slf4j
@Service
public class AreaService {

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    /**
     * 省份、地域-业务逻辑处理
     *
     * @param businessRouteValue
     * @param messageRouteAccountParams
     */
    public void provinceBusiness(BusinessRouteValue businessRouteValue, AccountBaseInfo messageRouteAccountParams) {

        //如果是国际运营商
        if ("INTL".equals(messageRouteAccountParams.getBusinessCarrier())) {
            String areaName = NumberUtils.getInternationalAreaName(businessRouteValue.getPhoneNumber());
            businessRouteValue.setAreaName(StringUtils.isEmpty(areaName) ? "未知" : areaName);
            String areaCode = "" + NumberUtils.getCountryCode(businessRouteValue.getPhoneNumber());
            businessRouteValue.setAreaCode(StringUtils.isEmpty(areaCode) ? "00" : areaCode);
            return;
        }

        //根据手机号前7位获取国内省份信息
        Object province = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 7));
        if (StringUtils.isEmpty(province)) {
            businessRouteValue.setAreaCode(province.toString().split("-")[0]);
            businessRouteValue.setAreaName(province.toString().split("-")[1]);
            businessRouteValue.setCityName("未知");
        } else {
            businessRouteValue.setAreaCode("00");
            businessRouteValue.setAreaName("未知");
            businessRouteValue.setCityName("未知");
        }
    }
}
