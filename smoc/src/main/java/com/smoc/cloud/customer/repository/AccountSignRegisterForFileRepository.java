package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.qo.ExportRegisterModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.entity.AccountSignRegisterForFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountSignRegisterForFileRepository extends JpaRepository<AccountSignRegisterForFile, String> {

    /**
     * 分页查询
     *
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
     *
     * @param pageParams
     * @return
     */
    PageList<ExportModel> export(PageParams<ExportModel> pageParams);

    /**
     * 为导出数据生成报备订单号，并改变报备数据状态
     *
     * @param exportRegisterModel
     */
    void register(ExportRegisterModel exportRegisterModel);

    @Modifying
    @Query(value = "update account_sign_register_for_file set REGISTER_STATUS=:status where REGISTER_ORDER_NO = :registerOrderNo ", nativeQuery = true)
    void updateRegisterStatusByOrderNo(@Param("registerOrderNo") String registerOrderNo, @Param("status") String status);

    /**
     * 根据报备订单号查询导出数据
     *
     * @param registerOrderNo
     * @return
     */
    PageList<ExportModel> query(PageParams pageParams, String registerOrderNo);

    List<AccountSignRegisterForFile> findByNumberSegmentAndRegisterStatus(String reportNumber, String s);
}
