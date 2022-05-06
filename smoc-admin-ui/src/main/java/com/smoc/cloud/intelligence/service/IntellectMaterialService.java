package com.smoc.cloud.intelligence.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.intelligence.remote.IntellectMaterialFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IntellectMaterialService {

    @Autowired
    private IntellectMaterialFeign intellectMaterialFeign;

    /**
     * 查询素材
     *
     * @return
     */
    public ResponseData<List<IntellectMaterialValidator>> getMaterial(String materialTypeId) {
        try {
            ResponseData<List<IntellectMaterialValidator>> data = intellectMaterialFeign.getMaterial(materialTypeId);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
