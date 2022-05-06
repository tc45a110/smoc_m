package com.smoc.cloud.intelligence.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.repository.IntellectMaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IntellectMaterialService {

    @Resource
    private IntellectMaterialRepository intellectMaterialRepository;

    public ResponseData<List<IntellectMaterial>> findIntellectMaterialByMaterialTypeId(String materialTypeId) {
        List<IntellectMaterial> list = intellectMaterialRepository.findIntellectMaterialByMaterialTypeId(materialTypeId);
        return ResponseDataUtil.buildSuccess(list);
    }
}
