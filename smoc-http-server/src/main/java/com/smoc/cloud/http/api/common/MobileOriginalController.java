package com.smoc.cloud.http.api.common;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.MobileOriginalRequestParams;
import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;

import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.service.MobileOriginalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 获取上行短信
 */
@Slf4j
@RestController
@RequestMapping("mobile/original")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MobileOriginalController {

    @Autowired
    private MobileOriginalService mobileOriginalService;

    /**
     * 获取上行短信
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getMobileOriginal", method = RequestMethod.POST)
    public ResponseData<List<MobileOriginalResponseParams>> getMobileOriginal(@RequestBody MobileOriginalRequestParams params) {

        log.info("[获取上行短信]：{}", new Gson().toJson(params));

        if (!ValidatorUtil.validate(params)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(params));
        }

        return mobileOriginalService.getMobileOriginal(params);
    }
}
