package com.smoc.cloud.http.service;


import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.ReportBatchParams;
import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import com.smoc.cloud.common.http.server.message.request.ReportStatusRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportStatusService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    /**
     * 根据账号获取状态报告 每次最多返回1000条
     *
     * @param params
     * @return
     */
    public ResponseData<List<ReportResponseParams>> getReport(ReportStatusRequestParams params) {

        //异步保存请求记录
       // systemHttpApiRequestService.save(params.getMsgId(), params.getAccount(), "getReport", new Gson().toJson(params));

        List<ReportResponseParams> reports = messageRepository.getReport(params);

        //把返回的条数删除
        if(null != reports && reports.size()>0) {
            deleteReportResponseParams(reports);
        }
        return ResponseDataUtil.buildSuccess(reports);
    }

    /**
     * 根据账号、起始日期 获取状态报告 每次最多返回1000条
     *
     * @param params
     * @return
     */
    public ResponseData<List<ReportResponseParams>> getReportBatch(ReportBatchParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "getReportBatch", new Gson().toJson(params));
        List<ReportResponseParams> reports = messageRepository.getReportBatch(params);

        //把返回的条数删除
        if(null != reports && reports.size()>0) {
            deleteReportResponseParams(reports);
        }
        return ResponseDataUtil.buildSuccess(reports);
    }


    /**
     * 异步，批量删除状态报告
     *
     * @param reports
     */
    @Async
    @Transactional
    public void deleteReportResponseParams(List<ReportResponseParams> reports) {
        messageRepository.batchDeleteReports(reports);
    }

}
