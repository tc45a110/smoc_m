package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.reconciliation.model.ReconciliationAccountModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReconciliationRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<ReconciliationAccountModel> getEnterpriseBills(){

        return null;

    }


}
