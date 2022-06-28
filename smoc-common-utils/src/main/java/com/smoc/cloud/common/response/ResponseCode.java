package com.smoc.cloud.common.response;

/**
 * 系统公共编码、消息提示
 * 2019/3/29 14:29
 **/
public enum ResponseCode {

    //成功
    SUCCESS("0000", "操作成功！"),

    //业务逻辑错误
    ERROR("1000", "业务逻辑错误！"),
    PARAM_ERROR("1100", "参数输入错误!"),
    PARAM_QUERY_ERROR("1101", "该记录不存在!"),
    PARAM_CREATE_ERROR("1102", "无法重复创建，关键信息重复！"),
    PARAM_LINK_ERROR("1103", "链接错误！"),
    PRAM_SENSITIVEWORDS_ERROR("1104", "参数含有敏感词！"),
    PARAM_FORMAT_ERROR("1100", "参数规则输入错误!"),
    PARAM_ORDER_ERROR("1103", "订单号重复"),
    PARAM_ABLE_ERROR("1104", "余额不足"),
    PARAM_MULTIMEDIA_ERROR("1105", "多媒体文件过大"),
    PARAM_MULTIMEDIA_FILE_ERROR("1106", "多媒体文件生成异常"),
    PARAM_TEMPLATE_ERROR("1107", "普通模版中包含变量字符$"),
    PARAM_TEMPLATE_VARIABLE_ERROR("1108", "变量模版中不包含变量字符$"),
    PARAM_MOBILE_ERROR("1109", "发送号码不能为空!"),
    PARAM_TEMPLATE_NOT_EXIST_ERROR("1110", "模版不存在!"),
    PARAM_TEMPLATE_STATUS_ERROR("1111", "模版未经审核!"),
    PARAM_MOBILE_NUM_ERROR("1112", "单批次发送量超过500条!"),
    PARAM_MOBILE_LIMITER_ERROR("1113", "发送速率，到达了受限速率！"),
    PARAM_SUBMIT_LIMITER_ERROR("1114", "提交速率太快，到达了受限提交速率！"),

    //鉴权失败
    TOKEN_UNAUTH("2000", "认证、鉴权失败！"),
    TOKEN_ISEMPTY("2001", "token不能为空！"),
    TOKEN_INVALID("2002", "无效token！"),
    SIGN_REQUEST_ERROR("2003", "签名失败！"),
    SIGN_PARAM_FORMAT_ERROR("2004", "签名格式问题"),
    SIGN_ERROR("2005", "签名错误"),
    SIGN_LEGAL_ERROR("2006", "签名不合法"),
    ACCOUNT_BUSINESS_ERROR("2007", "当前账号不支持该业务"),


    USER_UNAUTH("3000", "用户无权限信息！"),
    USER_NOT_EXIST("3001", "用户不存在或已注销"),
    USER_PASSWORD_NULL("3002", "密码错误！"),
    USER_LOGIN_FAILURE("3003", "登录失败"),
    REQUEST_LEGAL_ERROR("3004", "非法请求"),
    REQUEST_IP_ERROR("3005", "IP访问受限"),


    //OAuth2 异常
    OAUTH2_EXCEPTION_UNAUTH("4000", "无权限访问！"),
    OAUTH2_EXCEPTION_GET_TOKEN("4001", "获取Token失败！"),
    OAUTH2_EXCEPTION_SERVER("4002", "服务器错误！"),

    EXCEPTION_IDCARD("5003", ""),

    //限流、熔断降级
    HYSTRIX("5000", "触发限流规则或熔断规则！"),
    HYSTRIX_REMOTE("5001", "触发熔断规则！"),
    NONCE_ERROR("5002", "signature-nonce重复请求！"),
    EXCEPTION_ID_CARD("5003", "身份证解析异常"),
    EXCEPTION_FACE("5004", "人像照片不能为空"),

    //系统异常
    SYSTEM_EXCEPTION("9000", "系统异常！");

    private String code;

    private String message;

    ResponseCode(String code, String message) {
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
