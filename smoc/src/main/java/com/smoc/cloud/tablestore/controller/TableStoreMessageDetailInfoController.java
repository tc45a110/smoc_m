package com.smoc.cloud.tablestore.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.tablestore.service.TableStoreMessageDetailInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 短信明细
 */
@Slf4j
@RestController
@RequestMapping("message/detail")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TableStoreMessageDetailInfoController {

    @Autowired
    private TableStoreMessageDetailInfoService tableStoreMessageDetailInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/tableStorePage", method = RequestMethod.POST)
    public ResponseData<PageList<TableStoreMessageDetailInfoValidator>> tableStorePage(@RequestBody PageParams<TableStoreMessageDetailInfoValidator> pageParams) {

        return tableStoreMessageDetailInfoService.tableStorePage(pageParams);
    }

}
