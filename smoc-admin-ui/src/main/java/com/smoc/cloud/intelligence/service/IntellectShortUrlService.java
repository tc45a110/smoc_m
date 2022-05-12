package com.smoc.cloud.intelligence.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.intelligence.remote.IntellectShortUrlFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class IntellectShortUrlService {

    @Autowired
    private IntellectShortUrlFeign intellectShortUrlFeign;

    /**
     * 查询模版
     *
     * @return
     */
    public ResponseData<PageList<IntellectShortUrlValidator>> page(PageParams<IntellectShortUrlValidator> pageParams) {
        try {
            PageList<IntellectShortUrlValidator> data = intellectShortUrlFeign.page(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存模版
     *
     * @return
     */
    public ResponseData save(IntellectShortUrlValidator intellectShortUrlValidator) {
        try {
            ResponseData data = intellectShortUrlFeign.save(intellectShortUrlValidator);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
