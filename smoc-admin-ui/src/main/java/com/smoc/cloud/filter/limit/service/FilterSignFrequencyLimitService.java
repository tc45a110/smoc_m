package com.smoc.cloud.filter.limit.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.filter.limit.remote.FilterSignFrequencyLimitFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FilterSignFrequencyLimitService {

    @Autowired
    private FilterSignFrequencyLimitFeignClient filterSignFrequencyLimitFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FilterSignFrequencyLimitValidator>> page(PageParams<FilterSignFrequencyLimitValidator> pageParams) {
        try {
            ResponseData<PageList<FilterSignFrequencyLimitValidator>> data = this.filterSignFrequencyLimitFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterSignFrequencyLimitValidator> findById(String id) {

        try {
            ResponseData<FilterSignFrequencyLimitValidator> data = this.filterSignFrequencyLimitFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @param filterSignFrequencyLimitValidator
     * @return
     */
    public ResponseData save(FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, String op) {

        try {
            ResponseData data = this.filterSignFrequencyLimitFeignClient.save(filterSignFrequencyLimitValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {

        try {
            ResponseData data = this.filterSignFrequencyLimitFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
