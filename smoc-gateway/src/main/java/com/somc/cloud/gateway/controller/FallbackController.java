package com.somc.cloud.gateway.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FallbackController {

    @GetMapping("/defaultFallback")
    public ResponseData defaultFallback() {

        ResponseData responseData = ResponseDataUtil.buildError(ResponseCode.HYSTRIX.getCode(), ResponseCode.HYSTRIX.getMessage());

        log.info("[请求熔断]数据:{}", new Gson().toJson(responseData));
        return responseData;
    }

    @GetMapping("/defaultError")
    public ResponseData defaultError() {

        ResponseData responseData = ResponseDataUtil.buildError(ResponseCode.SIGN_REQUEST_ERROR.getCode(), ResponseCode.SIGN_REQUEST_ERROR.getMessage());
        log.info("[请求错误]数据:{}", new Gson().toJson(responseData));
        return responseData;
    }
}
