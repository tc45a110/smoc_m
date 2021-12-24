package com.smoc.cloud.parameter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendBusinessParamValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendBusinessParamValue;
import com.smoc.cloud.parameter.repository.ParameterExtendBusinessParamValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ParameterExtendBusinessParamValueService {

    @Resource
    private ParameterExtendBusinessParamValueRepository parameterExtendBusinessParamValueRepository;

    /**
     * 根据业务id 查询业务扩展字段值
     *
     * @param businessId
     * @return
     */
    public ResponseData<List<ParameterExtendBusinessParamValue>> findParameterExtendBusinessParamValueByBusinessId(String businessId) {

        List<ParameterExtendBusinessParamValue> data = parameterExtendBusinessParamValueRepository.findParameterExtendBusinessParamValueByBusinessId(businessId);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param list       要保存的数据列表
     * @param businessId 业务id
     * @return
     */
    @Transactional
    public ResponseData save(List<ParameterExtendBusinessParamValueValidator> list, String businessId) {

        parameterExtendBusinessParamValueRepository.deleteByBusinessId(businessId);
        parameterExtendBusinessParamValueRepository.batchSave(list);

        //记录日志
        log.info("[业务扩展字段][批量保存]数据:{}", JSON.toJSONString(list));
        return ResponseDataUtil.buildSuccess();
    }

}
