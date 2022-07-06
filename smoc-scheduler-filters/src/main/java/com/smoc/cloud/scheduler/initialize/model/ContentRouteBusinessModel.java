package com.smoc.cloud.scheduler.initialize.model;

import com.smoc.cloud.scheduler.initialize.entity.AccountContentRoute;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账号内容路由业务模型
 * 1、移动、联通、电信、国际
 * 2、
 */
public class ContentRouteBusinessModel {

    private AccountContentRoute cmccAccountContentRoute = null;

    private AccountContentRoute telcAccountContentRoute = null;

    private AccountContentRoute unicAccountContentRoute = null;

    private AccountContentRoute intlAccountContentRoute = null;

    /**
     * 根据内容路由到通道Id
     *
     * @return
     */
    public String route(String carrier, String areaCode, String phoneNumber, String content) {
        AccountContentRoute accountContentRoute = null;
        if ("CMCC".equals(carrier)) {
            if (null == cmccAccountContentRoute) {
                return null;
            }
            accountContentRoute = cmccAccountContentRoute;
        }

        if ("TELC".equals(carrier)) {
            if (null == telcAccountContentRoute) {
                return null;
            }
            accountContentRoute = telcAccountContentRoute;

        }

        if ("UNIC".equals(carrier)) {
            if (null == unicAccountContentRoute) {
                return null;
            }
            accountContentRoute = unicAccountContentRoute;
        }

        if ("INTL".equals(carrier)) {
            if (null == intlAccountContentRoute) {
                return null;
            }
            accountContentRoute = unicAccountContentRoute;
        }

        if (null == accountContentRoute) {
            return null;
        }

        //匹配路由内容 匹配则路由
        String routeContent = accountContentRoute.getRouteContent();
        Pattern contentPattern = Pattern.compile(routeContent);
        Matcher contentMatcher = contentPattern.matcher(content);
        if (!contentMatcher.find()) {
            return null;
        }

        //反向路由内容，包含则不路由
        String routeReverseContent = accountContentRoute.getRouteReverseContent();
        Pattern reverseContentPattern = Pattern.compile(routeReverseContent);
        Matcher reverseContentMatcher = reverseContentPattern.matcher(content);
        if (reverseContentMatcher.find()) {
            return null;
        }

        //号段路由 匹配则路由
        String mobileNum = accountContentRoute.getMobileNum();
        if (!StringUtils.isEmpty(mobileNum)) {
            Pattern mobilePattern = Pattern.compile(mobileNum);
            Matcher mobileMatcher = mobilePattern.matcher(phoneNumber);
            if (!mobileMatcher.find()) {
                return null;
            }
        }

        //短信长度 小于 minContent 则路由
        Integer minContent = accountContentRoute.getMinContent();
        if (null != minContent && 0 != minContent) {
            Integer contentLength = routeContent.length();
            if (minContent < contentLength) {
                return null;
            }
        }

        //短信长度 大于 maxContent 则路由
        Integer maxContent = accountContentRoute.getMaxContent();
        if (null != maxContent && 0 != maxContent) {
            Integer contentLength = routeContent.length();
            if (maxContent > contentLength) {
                return null;
            }
        }

        //业务区域
        Set<String> supportAreaCodes = accountContentRoute.getSupportAreaCodes();
        if (null == supportAreaCodes || supportAreaCodes.size() < 1) {
            return null;
        }
        if (!supportAreaCodes.contains(areaCode)) {
            return null;
        }

        return accountContentRoute.getRouteChannelId();
    }

    public void add(AccountContentRoute accountContentRoute) {
        //移动
        if ("CMCC".equals(accountContentRoute.getCarrier())) {
            cmccAccountContentRoute = accountContentRoute;
        }
        //电信
        if ("TELC".equals(accountContentRoute.getCarrier())) {
            telcAccountContentRoute = accountContentRoute;
        }
        //联通
        if ("UNIC".equals(accountContentRoute.getCarrier())) {
            unicAccountContentRoute = accountContentRoute;
        }
        //国际
        if ("INTL".equals(accountContentRoute.getCarrier())) {
            intlAccountContentRoute = accountContentRoute;
        }
    }

}
