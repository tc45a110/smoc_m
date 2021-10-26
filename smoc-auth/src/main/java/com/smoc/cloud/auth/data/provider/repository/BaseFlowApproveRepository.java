package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseFlowApprove;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BaseFlowApproveRepository extends CrudRepository<BaseFlowApprove, String>, JpaRepository<BaseFlowApprove, String> {

    /**
     * 审核记录
     * @param flowApproveValidator
     * @return
     */
    List<FlowApproveValidator> checkRecord(FlowApproveValidator flowApproveValidator);
}
