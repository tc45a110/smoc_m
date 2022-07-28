package com.smoc.cloud.iot.packages.service;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.packages.entity.IotPackageCard;
import com.smoc.cloud.iot.packages.repository.IotPackageCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IotPackageCardService {

    @Resource
    private IotPackageCardRepository iotPackageCardRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotPackageCardValidator>> page(PageParams<IotPackageCardValidator> pageParams) {
        PageList<IotPackageCardValidator> page = iotPackageCardRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotPackageCardValidator> findById(String id) {
        Optional<IotPackageCard> data = iotPackageCardRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotPackageCardValidator validator = new IotPackageCardValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        return ResponseDataUtil.buildSuccess(validator);
    }


    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> listCardsByPackageId(String account,String packageId) {
        List<IotFlowCardsPrimaryInfoValidator> list = this.iotPackageCardRepository.listCardsByPackageId(account,packageId);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 保存或修改
     *
     * @param cards
     * @return
     */
    @Transactional
    public ResponseData save(List<IotPackageCardValidator> cards) {
        return ResponseDataUtil.buildSuccess();
    }


}
