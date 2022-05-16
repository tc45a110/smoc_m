package com.smoc.cloud.intellect.service;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import com.smoc.cloud.intellect.remote.IntellectMaterialTypeFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IntellectMaterialTypeService {

    @Autowired
    private IntellectMaterialTypeFeign intellectMaterialTypeFeign;

    /**
     * 查询素材分类
     *
     * @return
     */
    public ResponseData<List<IntellectMaterialTypeValidator>> findIntellectMaterialTypeByParentIdAndStatus(String parentId) {
        try {
            ResponseData<List<IntellectMaterialTypeValidator>> data = intellectMaterialTypeFeign.findIntellectMaterialTypeByParentIdAndStatus(parentId);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    public ResponseData save(IntellectMaterialTypeValidator intellectMaterialTypeValidator, String op) {
        try {
            ResponseData data = intellectMaterialTypeFeign.save(intellectMaterialTypeValidator, op);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IntellectMaterialTypeValidator> findById(String id) {
        try {
            ResponseData data = intellectMaterialTypeFeign.findById(id);
            //log.info("[findById]:{}",new Gson().toJson(data));
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销或启用
     *
     * @param id
     * @param status
     * @return
     */
    public ResponseData cancel(String id, String status) {
        try {
            ResponseData data = intellectMaterialTypeFeign.cancel(id, status);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
