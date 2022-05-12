package com.smoc.cloud.intelligence.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.intelligence.entity.IntellectShortUrl;
import com.smoc.cloud.intelligence.repository.IntellectShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Slf4j
@Service
public class IntellectShortUrlService {

    @Resource
    private IntellectShortUrlRepository intellectShortUrlRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectShortUrlValidator> page(PageParams<IntellectShortUrlValidator> pageParams) {
        return intellectShortUrlRepository.page(pageParams);
    }

    /**
     * 保存或修改
     *
     * @param intellectShortUrlValidator
     * @return
     */
    @Transactional
    public ResponseData save(IntellectShortUrlValidator intellectShortUrlValidator) {

        IntellectShortUrl entity = new IntellectShortUrl();
        BeanUtils.copyProperties(intellectShortUrlValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectShortUrlValidator.getCreatedTime()));

        //记录日志
        log.info("[智能短信][保存短链][{}]数据:{}", JSON.toJSONString(entity));
        intellectShortUrlRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }
}
