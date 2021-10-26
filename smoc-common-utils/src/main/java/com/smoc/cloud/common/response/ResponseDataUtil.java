package com.smoc.cloud.common.response;

/**
 * 带实体的统一返回 ResponseData
 * 2019/3/29 14:29
 */
public class ResponseDataUtil {

    public static <T> ResponseData<T> buildSuccess(T data) {
        return new ResponseData<T>(ResponseCode.SUCCESS, data);
    }

    public static ResponseData buildSuccess() {
        return new ResponseData(ResponseCode.SUCCESS);
    }

    public static ResponseData buildSuccess(String message) {
        return new ResponseData(ResponseCode.SUCCESS.getCode(), message);
    }

    public static ResponseData buildSuccess(String code, String message) {
        return new ResponseData(code, message);
    }

    public static <T> ResponseData buildSuccess(String code, String message, T data) {
        return new ResponseData<T>(code, message, data);
    }

    public static ResponseData buildSuccess(ResponseCode responseCode) {
        return new ResponseData(responseCode);
    }

    public static <T> ResponseData buildError(T data) {
        return new ResponseData<T>(ResponseCode.ERROR, data);
    }

    public static <T> ResponseData buildError(ResponseCode responseCode, T data) {
        return new ResponseData<T>(responseCode, data);
    }

    public static ResponseData buildError() {
        return new ResponseData(ResponseCode.ERROR);
    }

    public static ResponseData buildError(String message) {
        return new ResponseData(ResponseCode.ERROR.getCode(), message);
    }

    public static ResponseData buildError(String code, String message) {
        return new ResponseData(code, message);
    }

    public static <T> ResponseData buildError(String code, String message, T data) {
        return new ResponseData<T>(code, message, data);
    }

    public static ResponseData buildError(ResponseCode responseCode) {
        return new ResponseData(responseCode);
    }
}
