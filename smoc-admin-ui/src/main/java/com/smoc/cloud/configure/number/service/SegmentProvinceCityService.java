package com.smoc.cloud.configure.number.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.configure.number.remote.SegmentProvinceCityFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 省号码管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SegmentProvinceCityService {

    @Autowired
    private SegmentProvinceCityFeignClient segmentProvinceCityFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemSegmentProvinceCityValidator>> page(PageParams<SystemSegmentProvinceCityValidator> pageParams) {
        try {
            PageList<SystemSegmentProvinceCityValidator> pageList = this.segmentProvinceCityFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<SystemSegmentProvinceCityValidator> findById(String id) {
        try {
            ResponseData<SystemSegmentProvinceCityValidator> data = this.segmentProvinceCityFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, String op) {

        try {
            ResponseData data = this.segmentProvinceCityFeignClient.save(systemSegmentProvinceCityValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.segmentProvinceCityFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量导出
     * @param systemSegmentProvinceCityValidator
     * @return
     */
    public ResponseData batchSave(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator) {
        try {
            this.segmentProvinceCityFeignClient.batchSave(systemSegmentProvinceCityValidator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
