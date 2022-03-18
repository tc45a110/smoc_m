package com.smoc.cloud.statistics.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.statistics.remote.StatisticsMessageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsMessageService {

    @Autowired
    private StatisticsMessageFeignClient statisticsMessageFeignClient;


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> page(PageParams<MessageDetailInfoValidator> pageParams) {
        try {
            ResponseData<PageList<MessageDetailInfoValidator>> pageList = this.statisticsMessageFeignClient.page(pageParams);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
