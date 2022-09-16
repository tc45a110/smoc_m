package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import com.smoc.cloud.parameter.errorcode.service.SystemErrorCodeService;
import com.smoc.cloud.tablestore.repository.TableStoreMessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信明细
 */
@Slf4j
@Service
public class MessageDetailInfoService {

    @Resource
    private MessageDetailInfoRepository messageDetailInfoRepository;

    @Resource
    private SystemErrorCodeService systemErrorCodeService;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> page(PageParams<MessageDetailInfoValidator> pageParams){

        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.page(pageParams);

        //匹配错误码描述
        List<MessageDetailInfoValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageDetailInfoValidator info :list){
                if(!StringUtils.isEmpty(info.getCustomerStatus())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getCustomerStatus(),info.getCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setCustomerStatus(info.getCustomerStatus()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 统计自服务平台短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> servicerPage(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.servicerPage(pageParams);

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.webTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.httpTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 单条短信发送记录
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> sendMessageList(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.sendMessageList(pageParams);

        //匹配错误码描述
        List<MessageDetailInfoValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageDetailInfoValidator info :list){
                if(!StringUtils.isEmpty(info.getCustomerStatus())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getCustomerStatus(),info.getCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setCustomerStatus(info.getCustomerStatus()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据企业实时查询成功发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    public ResponseData<Map<String, Object>> statisticEnterpriseSendMessage(MessageDetailInfoValidator messageDetailInfoValidator) {
        Map<String, Object> map = new HashMap<>();

        //短信成功发送总量
        Map<String, Object>  messageSendTotal = messageDetailInfoRepository.statisticEnterpriseSendMessage(messageDetailInfoValidator);
        map.put("SUCCESS_SEND_SUM",messageSendTotal.get("SUCCESS_SEND_SUM"));

        //短信发送总量
        Map<String, Object>  total = messageDetailInfoRepository.statisticEnterpriseTotalMessage(messageDetailInfoValidator);
        map.put("TOTAL_SEND_SUM",total.get("SUCCESS_SEND_SUM"));

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 消息状态码统计查询
     * @param params
     * @return
     */
    public ResponseData<PageList<MessageCodeValidator>> messageCcodeStautsList(PageParams<MessageCodeValidator> params) {
        PageList<MessageCodeValidator> page = messageDetailInfoRepository.messageCcodeStautsList(params);

        //匹配错误码描述
        List<MessageCodeValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageCodeValidator info :list){
                if(!StringUtils.isEmpty(info.getCodeStatus())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getCodeStatus(),info.getCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setCodeStatus(info.getCodeStatus()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);
    }
}
