package com.smoc.cloud.common.response;

import java.io.Serializable;

/**
 * 统一系统返回对象
 * 2019/3/29 14:29
 **/
public class ResponseData<T> implements Serializable {

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    public ResponseData() {
    }

    public ResponseData(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public ResponseData(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
