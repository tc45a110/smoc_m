package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.entity.IotFlowCardsPrimaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IotFlowCardsPrimaryInfoRepository extends JpaRepository<IotFlowCardsPrimaryInfo, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotFlowCardsPrimaryInfoValidator> page(PageParams<IotFlowCardsPrimaryInfoValidator> pageParams);

    List<IotFlowCardsPrimaryInfo> findByMsisdnOrImsiOrIccid(String msisdn, String imsi, String iccid);

    /**
     * 根据账号及msisdn 查询物联网卡基本信息
     * @param account
     * @param msisdn
     * @return
     */
    SimBaseInfoResponse findSimByAccountAndMsisdn(String account, String msisdn);

    /**
     * 码号信息批量查询
     * @param account
     * @param msisdns
     * @return
     */
    List<SimBaseInfoResponse> queryBatchSimBaseInfo(String account, List<String> msisdns);

    @Modifying
    @Query(value = "update iot_flow_cards_primary_info set CARD_STATUS=:status where ID = :id ", nativeQuery = true)
    void forbidden(@Param("id") String id, @Param("status") String status);
}