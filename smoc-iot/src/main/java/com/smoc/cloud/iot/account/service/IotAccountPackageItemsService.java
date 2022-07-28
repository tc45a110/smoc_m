package com.smoc.cloud.iot.account.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.iot.validator.AccountPackage;
import com.smoc.cloud.common.iot.validator.IotAccountPackageItemsValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.account.entity.IotAccountPackageItems;
import com.smoc.cloud.iot.account.repository.IotAccountPackageItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IotAccountPackageItemsService {

    @Resource
    private IotAccountPackageItemsRepository iotAccountPackageItemsRepository;

    /**
     * 查询业务账号套餐及未使用套餐
     * @param account
     * @return
     */
    public ResponseData<List<IotPackageInfoValidator>> list(String account) {
        List<IotPackageInfoValidator> list = iotAccountPackageItemsRepository.list(account);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 查询账号配置得套餐
     *
     * @param account
     * @return
     */
    public ResponseData<List<IotPackageInfoValidator>> listAccountPackages(String account){
        List<IotPackageInfoValidator> list = iotAccountPackageItemsRepository.listAccountPackages(account);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotAccountPackageItemsValidator> findById(String id) {
        Optional<IotAccountPackageItems> data = iotAccountPackageItemsRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotAccountPackageItemsValidator validator = new IotAccountPackageItemsValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        return ResponseDataUtil.buildSuccess(validator);
    }

    /**
     * 保存或修改
     *
     * @return
     */
    @Transactional
    public ResponseData save(AccountPackage accountPackage) {
        log.info("[accountPackage]:{}", new Gson().toJson(accountPackage));
        iotAccountPackageItemsRepository.insertAccountPackageCards(accountPackage.getAccount(), accountPackage.getPackageIds());
        return ResponseDataUtil.buildSuccess();
    }

}
