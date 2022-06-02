package com.smoc.cloud.main;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.index.CheckRemindModel;
import com.smoc.cloud.statistics.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页提醒
 **/
@Slf4j
@RestController
public class CheckRemindController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 签名资质、web模板、待下发短信提醒
     *
     * @return
     */
    @RequestMapping(value = "/index/remind/check", method = RequestMethod.GET)
    public CheckRemindModel remindCheck(HttpServletRequest request) {


        CheckRemindModel checkRemindModel = new CheckRemindModel();

        ResponseData<CheckRemindModel> data = statisticsService.remindCheck(checkRemindModel);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return checkRemindModel;
        }

        return data.getData();
    }

}
