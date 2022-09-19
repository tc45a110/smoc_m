package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.configure.channel.remote.ChannelFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * 通道管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelService {

    @Autowired
    private ChannelFeignClient channelFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ChannelBasicInfoQo>> page(PageParams<ChannelBasicInfoQo> pageParams) {
        try {
            PageList<ChannelBasicInfoQo> pageList = this.channelFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<ChannelBasicInfoValidator> findChannelById(String id) {
        try {
            ResponseData<ChannelBasicInfoValidator> data = this.channelFeignClient.findChannelById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(ChannelBasicInfoValidator channelBasicInfoValidator, String op) {

        try {
            ResponseData data = this.channelFeignClient.save(channelBasicInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param channelId
     * @return
     */
    public ResponseData<ChannelBasicInfoValidator> findById(String channelId) {
        try {
            ResponseData<ChannelBasicInfoValidator> data = this.channelFeignClient.findById(channelId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查看通道发送量统计
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticChannelSendNumber(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.channelFeignClient.statisticChannelSendNumber(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        ;

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);

        return accountStatisticSendData;
    }

    /**
     * 通道投诉率统计
     * @param statisticComplaintData
     * @return
     */
    public AccountStatisticComplaintData statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData) {
        ResponseData<List<AccountStatisticComplaintData>> responseData = this.channelFeignClient.statisticComplaintMonth(statisticComplaintData);
        List<AccountStatisticComplaintData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticComplaintData::getMonth).toArray(String[]::new);
        //投诉率
        String[] complaint = list.stream().map(AccountStatisticComplaintData::getComplaint).toArray(String[]::new);
        ;

        AccountStatisticComplaintData accountStatisticComplaintData = new AccountStatisticComplaintData();
        accountStatisticComplaintData.setMonthArray(month);
        accountStatisticComplaintData.setComplaintArray(complaint);

        return accountStatisticComplaintData;
    }

    /**
     * 通道账号使用明细
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ChannelAccountInfoQo>> channelAccountList(PageParams<ChannelAccountInfoQo> pageParams) {
        try {
            ResponseData<PageList<ChannelAccountInfoQo>> pageList = this.channelFeignClient.channelAccountList(pageParams);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 通道接口参数查询
     * @param params
     * @return
     */
    public ResponseData<PageList<ChannelInterfaceInfoQo>> channelInterfacePage(PageParams<ChannelInterfaceInfoQo> params) {
        try {
            ResponseData<PageList<ChannelInterfaceInfoQo>> pageList = this.channelFeignClient.channelInterfacePage(params);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询所有通道
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> queryChannelAll(ChannelBasicInfoValidator channelBasicInfoValidator) {
        try {
            ResponseData<List<ChannelBasicInfoQo>> pageList = this.channelFeignClient.queryChannelAll(channelBasicInfoValidator);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
