package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.entity.AccountSignRegisterForFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountSignRegisterForFileRepository extends JpaRepository<AccountSignRegisterForFile, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<AccountSignRegisterForFileValidator> page(PageParams<AccountSignRegisterForFileValidator> pageParams);

    /**
     * 根据运营商，统计未报备得条数
     *
     * @return
     */
    List<CarrierCount> countByCarrier();

    /**
     * 查询导出数据
     * @param pageParams
     * @return
     */
    PageList<ExportModel> export(PageParams<ExportModel> pageParams);
}
