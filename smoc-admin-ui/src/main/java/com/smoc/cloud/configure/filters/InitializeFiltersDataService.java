package com.smoc.cloud.configure.filters;

import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializeFiltersDataService {

    @Autowired
    private InitializeFiltersFeign initializeFiltersFeign;

    /**
     *
     * @return
     */
    public ResponseData initialize(InitializeFiltersData initializeFiltersData) {
        try {
            ResponseData data = initializeFiltersFeign.initialize(initializeFiltersData);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
