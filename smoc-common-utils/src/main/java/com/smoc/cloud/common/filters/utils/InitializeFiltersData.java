package com.smoc.cloud.common.filters.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InitializeFiltersData {

    //系统黑名单
    private String reloadBlackList = "0";

    //系统白名单
    private String reloadWhiteList = "0";;

    //业务账户过滤参数
    private String reloadAccountFilterParams = "0";;

    //系统敏感词
    private String reloadSystemSensitiveWords = "0";;

    //账户关键词
    private String reloadAccountKeyWords = "0";;

    private String reloadTypeKeyWords = "0";;

}
