package com.smoc.cloud.filters.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CircuitBreakerController {
    /**
     * 断路器
     * @return
     */
    @RequestMapping(value = "/fallback")
    public ResponseData fallback() {
        log.info("[触发熔断]-----------------------------------------");
        return ResponseDataUtil.buildError(FilterResponseCode.REQUEST_CIRCUIT_BREAKER_ERROR.getCode(),FilterResponseCode.REQUEST_CIRCUIT_BREAKER_ERROR.getMessage());
    }

}
