package com.smoc.cloud.common.filters.utils;

import com.smoc.cloud.common.auth.qo.DictType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InitializeFiltersData {

    //系统黑名单
    private String reloadBlackList = "0";

    //系统白名单
    private String reloadWhiteList = "0";

    //业务账户过滤参数
    private String reloadAccountFilterParams = "0";

    //系统敏感词
    private String reloadSystemSensitiveWords = "0";

    //系统审核词
    private String reloadSystemCheckWords = "0";

    //系统超级白词
    private String reloadSystemSuperWhiteWords = "0";

    //系统洗黑白词
    private String reloadSystemWhiteBlackWords = "0";

    //系统免审白词
    private String reloadSystemNoCheckWhiteWords = "0";

    //系统正则白词
    private String reloadSystemRegularWhiteWords = "0";

    //行业敏感词
    private String reloadInfoSensitiveWords = "0";
    //行业分类
    private DictType infoType;

    //业务账号敏感词
    private String reloadAccountSensitiveWords = "0";

    //业务账号审核词
    private String reloadAccountCheckWords = "0";

    //业务账号超级白词
    private String reloadAccountSuperWhiteWords = "0";

    //业务账号洗黑白词
    private String reloadAccountWhiteBlackWords = "0";

    //系统免审核白词
    private String reloadAccountNoCheckWhiteWords = "0";

    //账号正则白词
    private String reloadAccountRegularWhiteWords = "0";


}
