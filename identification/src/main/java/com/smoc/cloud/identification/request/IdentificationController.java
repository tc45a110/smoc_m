package com.smoc.cloud.identification.request;

import com.google.gson.Gson;
import com.smoc.cloud.common.identification.RequestModel;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.identification.service.IdentificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 身份认证
 */
@Slf4j
@RestController
@RequestMapping("/idCard")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class IdentificationController {

    @Autowired
    private IdentificationService identificationService;

    /**
     * 验证身份证姓名、身份证号
     *
     * @return
     */
    @RequestMapping(value = "/name", method = RequestMethod.POST)
    public ResponseData name(@RequestBody RequestModel requestModel) {

        log.info("[身份验证]请求数据：{}",new Gson().toJson(requestModel));
        return identificationService.identification(requestModel);
    }

    /**
     * 验证身份证姓名、身份证号、人脸照片
     *
     * @return
     */
    @RequestMapping(value = "/name/face", method = RequestMethod.POST)
    public ResponseData face(@RequestBody RequestModel requestModel) {

        //log.info("[身份验证]请求数据：{}",new Gson().toJson(requestModel));
        return identificationService.identificationFace(requestModel);
    }

}
