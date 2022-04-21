package com.smoc.cloud.http.api.common;


import com.google.gson.Gson;
import com.smoc.cloud.http.entity.PushReportParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;


@Slf4j
@RestController
@RequestMapping("report")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PushReportController {

    /**
     * http://localhost:18091/http-server/report/pushReport
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pushReport", method = RequestMethod.POST)
    public String pushReport(@RequestBody PushReportParams params) {

        log.info("[推送过来的状态报告]：{}", new Gson().toJson(params));
        return "0000";
    }
}
