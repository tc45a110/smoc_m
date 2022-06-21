package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 业务账号操作类
 */
public interface BusinessAccountRepository extends CrudRepository<AccountBasicInfo, String>, JpaRepository<AccountBasicInfo, String> {

    /**
     * 查询-分页
     *
     * @param pageParams
     * @return
     */
    PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams);


    Iterable<AccountBasicInfo> findByAccountId(String account);

    /**
     * 注销、启用业务账号
     *
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update account_base_info set ACCOUNT_STATUS = :status where ACCOUNT_ID = :id", nativeQuery = true)
    void updateAccountStatusById(@Param("id") String id, @Param("status") String status);

    /**
     * 查询企业所有的业务账号
     *
     * @param enterpriseId
     * @return
     */
    List<AccountBasicInfoValidator> findBusinessAccountByEnterpriseId(String enterpriseId);

    /**
     * 根据业务类型查询企业所有的业务账号
     *
     * @param enterpriseId
     * @param businessType
     * @return
     */
    List<AccountBasicInfo> findBusinessAccountByEnterpriseIdAndBusinessType(String enterpriseId, String businessType);

    /**
     * 查询企业所有的业务账号
     *
     * @param accountBasicInfoValidator
     * @return
     */
    List<AccountBasicInfoValidator> findBusinessAccount(AccountBasicInfoValidator accountBasicInfoValidator);

    /**
     * 查询企业下的账户和余额
     *
     * @param messageAccountValidator
     * @return
     */
    List<MessageAccountValidator> messageAccountList(MessageAccountValidator messageAccountValidator);

    /**
     * 查询自服务平台发送账号列表
     *
     * @param params
     * @return
     */
    PageList<MessageAccountValidator> messageAccountInfoList(PageParams<MessageAccountValidator> params);

    /**
     * 账号按维度统计发送量
     *
     * @param statisticSendData
     * @return
     */
    List<AccountStatisticSendData> statisticAccountSendNumber(AccountStatisticSendData statisticSendData);

    /**
     * 账号投诉率统计
     *
     * @param statisticComplaintData
     * @return
     */
    List<AccountStatisticComplaintData> statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData);

    /**
     * 业务账号综合查询
     *
     * @param pageParams
     * @return
     */
    PageList<AccountInfoQo> accountAll(PageParams<AccountInfoQo> pageParams);

    /**
     * 查询账户列表根据接口类型
     * @param pageParams
     * @return
     */
    PageList<AccountBasicInfoValidator> accountByProtocol(PageParams<AccountBasicInfoValidator> pageParams);
}
