package com.smoc.cloud.iot.product.service;

import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.product.entity.IotProductCard;
import com.smoc.cloud.iot.product.repository.IotProductCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IotProductCardService {

    @Resource
    private IotProductCardRepository iotProductCardRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotProductCardValidator>> page(PageParams<IotProductCardValidator> pageParams) {
        PageList<IotProductCardValidator> page = iotProductCardRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotProductCardValidator> findById(String id) {
        Optional<IotProductCard> data = iotProductCardRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotProductCardValidator validator = new IotProductCardValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        return ResponseDataUtil.buildSuccess(validator);
    }

    /**
     * 保存或修改
     *
     * @param cards
     * @return
     */
    @Transactional
    public ResponseData save(List<IotProductCardValidator> cards) {
        return ResponseDataUtil.buildSuccess();
    }


}
