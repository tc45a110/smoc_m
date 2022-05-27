package com.smoc.cloud.filter.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.entity.FilterWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BlackRepository extends CrudRepository<FilterBlackList, String>, JpaRepository<FilterBlackList, String> {


    /**
     *  根据群id查询黑名单
     * @param pageParams
     * @return
     */
    PageList<FilterBlackListValidator> page(PageParams<FilterBlackListValidator> pageParams);

    /**
     * 查询组里是否存在手机号
     * @param groupId
     * @param mobile
     * @param status
     * @return
     */
    List<FilterBlackList> findByGroupIdAndMobileAndStatus(String groupId, String mobile, String status);

    /**
     * 批量保存
     * @param filterBlackListValidator
     */
    void bathSave(FilterBlackListValidator filterBlackListValidator);

    /**
     * 查询导出数据
     * @param pageParams
     * @return
     */
    List<ExcelModel> excelModel(PageParams<FilterBlackListValidator> pageParams);

    /**
     * 根据组id删除联系人
     * @param id
     */
    void deleteByGroupId(String id);

    /**
     * 导入投诉号码到黑名单
     * @param messageComplaintInfoValidator
     * @param groupComplaintId
     */
    void complaintBathSave(MessageComplaintInfoValidator messageComplaintInfoValidator, String groupComplaintId);

    List<FilterBlackList> findByEnterpriseIdAndGroupIdAndMobileAndStatus(String enterpriseId, String groupId, String mobile, String s);
}
