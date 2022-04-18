package com.smoc.cloud.http.service;


import com.smoc.cloud.common.http.server.message.request.ReportBatchParams;
import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import com.smoc.cloud.common.http.server.message.request.ReportStatusRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportStatusService {


    public ResponseData<List<ReportResponseParams>> getReportByOrderNo(ReportStatusRequestParams params) {

        List<ReportResponseParams> report = new ArrayList<>();
        ReportResponseParams model = new ReportResponseParams();
        model.setAccount("SWL102");
        model.setMobile("18510369887");
        model.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model.setReportTime("2022-04-23 14:25:26");
        model.setStatus("DELIVRD");
        report.add(model);

        ReportResponseParams model1 = new ReportResponseParams();
        model1.setAccount("SWL102");
        model1.setMobile("18560369887");
        model1.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model1.setReportTime("2022-04-23 14:25:26");
        model1.setStatus("DELIVRD");
        report.add(model1);

        ReportResponseParams model2 = new ReportResponseParams();
        model2.setAccount("SWL102");
        model2.setMobile("18580569887");
        model2.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model2.setReportTime("2022-04-23 14:25:26");
        model2.setStatus("DELIVRD");
        report.add(model2);
        return ResponseDataUtil.buildSuccess(report);
    }

    public ResponseData<List<ReportResponseParams>> getReportBatch(ReportBatchParams params) {

        List<ReportResponseParams> report = new ArrayList<>();
        ReportResponseParams model = new ReportResponseParams();
        model.setAccount("SWL102");
        model.setMobile("18510369887");
        model.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model.setReportTime("2022-04-23 14:25:26");
        model.setStatus("DELIVRD");
        report.add(model);

        ReportResponseParams model1 = new ReportResponseParams();
        model1.setAccount("SWL102");
        model1.setMobile("18560369887");
        model1.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model1.setReportTime("2022-04-23 14:25:26");
        model1.setStatus("DELIVRD");
        report.add(model1);

        ReportResponseParams model2 = new ReportResponseParams();
        model2.setAccount("SWL102");
        model2.setMobile("18580569887");
        model2.setMsgId("1c5979e910fe44598ab7bbe5f521439d");
        model2.setReportTime("2022-04-23 14:25:26");
        model2.setStatus("DELIVRD");
        report.add(model2);
        return ResponseDataUtil.buildSuccess(report);
    }

}
