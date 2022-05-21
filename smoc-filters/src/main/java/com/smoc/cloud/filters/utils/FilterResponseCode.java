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
    NO_CONFIG_NUMBER_BLACK_LIST("1100", "没有设置黑名单过滤层级！"),
    IS_EXIST_SYSTEM_NUMBER_BLACK_LIST("1101", "该号码在系统黑名单中!"),
    IS_EXIST_LOCAL_NUMBER_BLACK_LIST("1102", "该号码在本地黑名单中!"),
    IS_EXIST_THIRD_NUMBER_BLACK_LIST("1103", "该号码在第三方黑名单中!"),

    NUMBER_NO_CONFIG_FREQUENCY_LIMIT("1200", "没有设置单手机号发送频率限制参数，或配置不规范!"),
    NUMBER_FREQUENCY_LIMIT_PARAM("1201", "单手机号发送频率参数设置不规范!"),
    NUMBER_FREQUENCY_LIMIT("1202", "达到手机号发送频率设置的上限！"),
    NUMBER_BLACK_FILTER("1203", "手机号码被号码黑词拦截！"),
    NUMBER_REGULAR_FILTER("1204", "手机号码被号码正则拦截！"),


    DAILY_LIMIT_CARRIER("1301", "达到运营商日限量限制！"),
    TIME_LIMIT_CONFIG_ERROR("1302", "发送时间段参数配置错误！"),
    TIME_LIMIT("1303", "该时间不能发送短信！"),
    PARAM_LINK_ERROR("1103", "链接错误！"),
    PRAM_SENSITIVEWORDS_ERROR("1104", "参数含有敏感词！"),
    PARAM_ORDER_ERROR("1103", "订单号重复"),
    PARAM_ABLE_ERROR("1104", "余额不足"),
    PARAM_MULTIMEDIA_ERROR("1105", "多媒体文件过大"),
    PARAM_MULTIMEDIA_FILE_ERROR("1106", "多媒体文件生成异常"),
    PARAM_TEMPLATE_ERROR("1107", "普通模版中包含变量字符$"),
    PARAM_TEMPLATE_VARIABLE_ERROR("1108", "变量模版中不包含变量字符$"),
    PARAM_MOBILE_ERROR("1109", "发送号码不能为空!"),
    PARAM_TEMPLATE_NOT_EXIST_ERROR("1110", "模版不存在!"),
    PARAM_TEMPLATE_STATUS_ERROR("1111", "模版未经审核!"),
    PARAM_MOBILE_NUM_ERROR("1112", "单批次发送量超过1000条!"),
    PARAM_MOBILE_LIMITER_ERROR("1113", "发送速率，到达了受限速率！"),
    PARAM_SUBMIT_LIMITER_ERROR("1114", "提交速率太快，到达了受限提交速率！"),

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
