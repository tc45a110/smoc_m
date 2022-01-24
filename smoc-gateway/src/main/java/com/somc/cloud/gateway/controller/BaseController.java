package com.somc.cloud.gateway.controller;


import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;

public class BaseController {

    /**
     * 统一返回 数据格式校验错误
     * @param errorMessage
     * @return
     */
    public ResponseData errorResponseData(String errorMessage){
        ResponseData responseData = ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(),errorMessage);
        return responseData;
    }
}
