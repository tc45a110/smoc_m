package com.smoc.cloud.iot.account.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.iot.validator.AccountProduct;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductItemsValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.account.entity.IotUserProductInfo;
import com.smoc.cloud.iot.account.repository.IotUserProductInfoRepository;
import com.smoc.cloud.iot.product.entity.IotProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class IotUserProductInfoService {

    @Resource
    private IotUserProductInfoRepository iotUserProductInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotUserProductInfoValidator>> page(PageParams<IotUserProductInfoValidator> pageParams) {
        PageList<IotUserProductInfoValidator> page = iotUserProductInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 列表查询
     *
     * @param account
     * @return
     */
    public ResponseData<List<IotProductInfoValidator>> list(String account) {
        List<IotProductInfoValidator> list = iotUserProductInfoRepository.list(account);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotUserProductInfoValidator> findById(String id) {
        Optional<IotUserProductInfo> data = iotUserProductInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotUserProductInfoValidator validator = new IotUserProductInfoValidator();
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
    public ResponseData save(AccountProduct accountProduct) {
        log.info("[accountProduct]:{}", new Gson().toJson(accountProduct));
        iotUserProductInfoRepository.insertAccountProductCards(accountProduct.getAccount(), accountProduct.getProductIds());
        return ResponseDataUtil.buildSuccess();
    }

}
