package com.smoc.cloud.intelligence.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.intelligence.entity.IntellectCallbackShowReport;
import com.smoc.cloud.intelligence.entity.IntellectShortUrl;
import com.smoc.cloud.intelligence.repository.IntellectCallbackShowReportRepository;
import com.smoc.cloud.intelligence.repository.IntellectShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IntellectCallbackShowReportService {

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private IntellectShortUrlRepository intellectShortUrlRepository;

    @Resource
    private IntellectCallbackShowReportRepository intellectCallbackShowReportRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectCallbackShowReportValidator> page(PageParams<IntellectCallbackShowReportValidator> pageParams) {
        return intellectCallbackShowReportRepository.page(pageParams);
    }

    /**
     * 保存智能显示回执报告，并根据显示报告状态进行计费
     *
     * @param intellectCallbackShowReportValidator
     * @return
     */
    @Async
    @Transactional
    public void save(IntellectCallbackShowReportValidator intellectCallbackShowReportValidator) {

        IntellectCallbackShowReport entity = new IntellectCallbackShowReport();
        BeanUtils.copyProperties(intellectCallbackShowReportValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectCallbackShowReportValidator.getCreatedTime()));
        //记录日志
        log.info("[智能短信][智能显示回执][模版状态回调][{}]数据:{}", JSON.toJSONString(entity));

        //根据状态，进行扣费操作
        if (0 == intellectCallbackShowReportValidator.getStatus()) { //表示成功解析
            //查询原锻炼数据，里面有价格
            List<IntellectShortUrl> shortUrls = intellectShortUrlRepository.findIntellectShortUrlByTplIdAndCustIdAndAimCodeAndAimUrl(intellectCallbackShowReportValidator.getTplId().trim(), intellectCallbackShowReportValidator.getCustId(), intellectCallbackShowReportValidator.getAimCode().trim(), intellectCallbackShowReportValidator.getAimUrl().trim());
            //从冻结中扣除金额
            if (null != shortUrls && shortUrls.size() > 0) {
                IntellectShortUrl shortUrl = shortUrls.get(0);
                intellectShortUrlRepository.addSuccessAnalysis(shortUrl.getId(), 1);
                financeAccountRepository.unfreeze(shortUrl.getCustId(), shortUrl.getCurrentPrice());
                log.info("[智能短信][智能显示回执][扣费][成功计费]数据:{}", JSON.toJSONString(intellectCallbackShowReportValidator));
            } else {
                log.error("[智能短信][智能显示回执][扣费][计费失败]数据:{}", JSON.toJSONString(intellectCallbackShowReportValidator));
            }
        }

        //保存模版状态回执
        intellectCallbackShowReportRepository.saveAndFlush(entity);
    }
}
