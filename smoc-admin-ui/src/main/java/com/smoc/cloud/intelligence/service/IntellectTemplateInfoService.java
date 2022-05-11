package com.smoc.cloud.intelligence.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.remote.IntellectTemplateInfoFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class IntellectTemplateInfoService {

    @Autowired
    private IntellectTemplateInfoFeign intellectTemplateInfoFeign;

    /**
     * 查询模版
     *
     * @return
     */
    public ResponseData<PageList<IntellectTemplateInfoValidator>> page(PageParams<IntellectTemplateInfoValidator> pageParams) {
        try {
            PageList<IntellectTemplateInfoValidator> data = intellectTemplateInfoFeign.page(pageParams);
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
    public ResponseData save(IntellectTemplateInfoValidator intellectTemplateInfoValidator) {
        try {
            ResponseData data = intellectTemplateInfoFeign.save(intellectTemplateInfoValidator);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
