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
    PARAM_QUERY_ERROR("1101","该记录不存在!"),
    PARAM_CREATE_ERROR("1102", "无法重复创建，关键信息重复！"),
    PARAM_LINK_ERROR("1103", "链接错误！"),
    PRAM_SENSITIVEWORDS_ERROR("1104", "参数含有敏感词！"),

    //鉴权失败
    TOKEN_UNAUTH("2000", "认证、鉴权失败！"),
    TOKEN_ISEMPTY("2001","token不能为空！"),
    TOKEN_INVALID("2002","无效token！"),

    USER_UNAUTH("3000", "用户无权限信息！"),
    USER_NOT_EXIST("3001","用户不存在！"),
    USER_PASSWORD_NULL("3002","密码错误！"),
    USER_LOGIN_FAILURE("3003","登录失败"),


    //OAuth2 异常
    OAUTH2_EXCEPTION_UNAUTH("4000", "无权限访问！"),
    OAUTH2_EXCEPTION_GET_TOKEN("4001", "获取Token失败！"),
    OAUTH2_EXCEPTION_SERVER("4002", "服务器错误！"),

    //限流、熔断降级
    HYSTRIX("5000", "触发限流规则或熔断规则！"),
    HYSTRIX_REMOTE("5001", "触发熔断规则！"),

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
