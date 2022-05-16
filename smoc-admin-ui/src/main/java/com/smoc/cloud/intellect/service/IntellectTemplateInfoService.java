package com.smoc.cloud.intellect.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intellect.remote.IntellectTemplateInfoFeign;
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

    /**
     * 根据templateId 修改 tplId
     *
     * @param templateId
     * @param tplId
     * @return
     */
    public ResponseData updateTplIdAndStatus(String templateId, String tplId,Integer status) {
        try {
            ResponseData data = intellectTemplateInfoFeign.updateTplIdAndStatus(templateId, tplId,status);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * templateId 修改 check status
     *
     * @param status
     * @param templateId
     * @return
     */
    public ResponseData updateCheckStatus(String templateId, Integer status) {
        try {
            ResponseData data = intellectTemplateInfoFeign.updateCheckStatus(templateId, status);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * tplId 修改 check status
     *
     * @param status
     * @param tplId
     * @return
     */
    public ResponseData updateCheckStatusByTplId(String tplId, Integer status) {
        try {
            ResponseData data = intellectTemplateInfoFeign.updateCheckStatusByTplId(tplId, status);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * templateId 修改  status
     *
     * @param status
     * @param templateId
     * @return
     */
    public ResponseData updateStatusByTemplateId(String templateId, Integer status) {
        try {
            ResponseData data = intellectTemplateInfoFeign.updateStatusByTemplateId(templateId, status);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
