package com.smoc.cloud.filters.utils;

/**
 * 过滤响应状态吗
 */
public enum FilterResponseCode {

    //成功通过过滤
    SUCCESS("0000", "操作成功！"),

    //参数
    PARAM_FORMAT_ERROR("1000", "参数规则输入错误!"),

    //黑名单
    NO_CONFIG_NUMBER_BLACK_LIST("1100", "黑名单过滤层级设置数据格式错误！"),
    IS_EXIST_SYSTEM_NUMBER_BLACK_LIST("1101", "该号码在系统黑名单中!"),
    IS_EXIST_LOCAL_NUMBER_BLACK_LIST("1102", "该号码在本地黑名单中!"),
    IS_EXIST_THIRD_NUMBER_BLACK_LIST("1103", "该号码在第三方黑名单中!"),

    NUMBER_NO_CONFIG_FREQUENCY_LIMIT("1200", "没有设置单手机号发送频率限制参数，或配置不规范!"),
    NUMBER_FREQUENCY_LIMIT_PARAM("1201", "单手机号发送频率参数设置不规范!"),
    NUMBER_FREQUENCY_LIMIT("1202", "达到手机号发送频率设置的上限！"),
    NUMBER_BLACK_FILTER("1203", "手机号码被号码扩展字段拦截！"),
    NUMBER_REGULAR_FILTER("1204", "手机号码被号码正则拦截！"),


    MESSAGE_SENSITIVE_EXTEND_FILTER("1205", "被扩展字段敏感词拦截！"),
    MESSAGE_REGULAR_FILTER("1206", "被系统敏感词正则拦截！"),
    MESSAGE_SENSITIVE_FILTER("1207", "被系统敏感词拦截！"),
    MESSAGE_CHECK_FILTER("1208", "被系统审核词拦截！"),
    MESSAGE_INFO_TYPE_SENSITIVE_FILTER("1209", "被行业敏感词拦截！"),
    MESSAGE_ACCOUNT_SENSITIVE_FILTER("1210", "被业务账号敏感词拦截！"),
    MESSAGE_ACCOUNT_CHECK_FILTER("1211", "被业务账号审核词拦截！"),
    NUMBER_INDUSTRY_BLACK_FILTER("1212", "手机号码被行业黑名单拦截！"),
    MESSAGE_CHANNEL_SENSITIVE_FILTER("1213", "被通道敏感词拦截！"),

    LIMIT_DAILY_CARRIER("1301", "达到运营商日限量限制！"),
    LIMIT_TIME_CONFIG_ERROR("1302", "发送时间段参数配置错误！"),
    LIMIT_TIME("1303", "该时间不能发送短信！"),
    LIMIT_MASK_PROVINCE("1304", "该发送省份已被屏蔽！"),

    SIGN_IS_NULL("1400", "签名不能为空！"),
    SIGN_IS_NOT_FOUND("1401", "签名没有报备！"),
    TEMPLATE_IS_NOT_FOUND("1402", "没匹配到模版ID对应模版！"),

    REQUEST_LIMIT_ERROR("2000","请求达到限流上限"),
    REQUEST_CIRCUIT_BREAKER_ERROR("2001","触发熔断机制"),



    REQUEST_LEGAL_ERROR("3004", "非法请求"),
    REQUEST_IP_ERROR("3005", "IP访问受限");


    private String code;

    private String message;

    FilterResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
