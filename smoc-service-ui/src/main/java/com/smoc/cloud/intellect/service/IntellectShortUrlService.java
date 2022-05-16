package com.smoc.cloud.intellect.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.intellect.remote.IntellectShortUrlFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntellectShortUrlService {

    @Autowired
    private IntellectShortUrlFeign intellectShortUrlFeign;

    /**
     * 查询短链
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
     * 保存短链
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
