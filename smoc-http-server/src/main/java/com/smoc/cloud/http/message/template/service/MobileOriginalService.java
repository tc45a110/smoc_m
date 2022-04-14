package com.smoc.cloud.http.message.template.service;


import com.smoc.cloud.common.http.server.message.request.MobileOriginalRequestParams;
import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MobileOriginalService {


    public ResponseData<List<MobileOriginalResponseParams>> getMobileOriginal(MobileOriginalRequestParams params){

        List<MobileOriginalResponseParams> result = new ArrayList<>();
        MobileOriginalResponseParams model = new MobileOriginalResponseParams();
        model.setAccount("SWL102");
        model.setMobile("18513695412");
        model.setContent("ok");
        model.setExtNumber("2547");
        model.setAcceptTime("2022-01-13 17:55:32");
        result.add(model);

        MobileOriginalResponseParams model1 = new MobileOriginalResponseParams();
        model1.setAccount("SWL102");
        model1.setMobile("18513695412");
        model1.setContent("ok,ok");
        model1.setExtNumber("2547");
        model1.setAcceptTime("2022-01-13 17:55:32");
        result.add(model1);

        MobileOriginalResponseParams model2 = new MobileOriginalResponseParams();
        model2.setAccount("SWL102");
        model2.setMobile("18513695412");
        model2.setContent("ok,ok,ok");
        model2.setExtNumber("2547");
        model2.setAcceptTime("2022-01-13 17:55:32");
        result.add(model2);

        return ResponseDataUtil.buildSuccess(result);
    }
}
