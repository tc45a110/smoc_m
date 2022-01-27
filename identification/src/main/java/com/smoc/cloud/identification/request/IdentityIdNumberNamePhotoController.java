package com.smoc.cloud.identification.request;
import com.smoc.cloud.common.identification.RequestModel;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.identification.service.IdentificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 身份证号、姓名、头像照片认证
 */
@Slf4j
@RestController
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class IdentityIdNumberNamePhotoController {

    @Autowired
    private IdentificationService identificationService;

    /**
     * 验证身份证姓名、身份证号、人脸照片
     *
     * @return
     */
    @RequestMapping(value = "/idNumberNamePhoto", method = RequestMethod.POST)
    public ResponseData photo(@RequestBody RequestModel requestModel) {

        //log.info("[身份验证]请求数据：{}",new Gson().toJson(requestModel));
        return identificationService.identificationFace(requestModel);
    }

}
