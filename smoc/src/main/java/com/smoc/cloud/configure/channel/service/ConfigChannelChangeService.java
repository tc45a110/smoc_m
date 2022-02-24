package com.smoc.cloud.configure.channel.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChange;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChangeItem;
import com.smoc.cloud.configure.channel.repository.ConfigChannelChangeItemRepository;
import com.smoc.cloud.configure.channel.repository.ConfigChannelChangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 通道切换
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelChangeService {

    @Resource
    private ConfigChannelChangeItemRepository configChannelChangeItemRepository;


    @Resource
    private ConfigChannelChangeRepository configChannelChangeRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigChannelChangeValidator>> page(PageParams<ConfigChannelChangeValidator> pageParams) {

        PageList<ConfigChannelChangeValidator> page = configChannelChangeRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<ConfigChannelChangeValidator> findById(String id) {
        Optional<ConfigChannelChange> data = configChannelChangeRepository.findById(id);
        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigChannelChange entity = data.get();
        ConfigChannelChangeValidator configChannelChangeValidator = new ConfigChannelChangeValidator();
        BeanUtils.copyProperties(entity, configChannelChangeValidator);
        configChannelChangeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(configChannelChangeValidator);
    }

    /**
     * 保存或修改
     *
     * @param configChannelChangeValidator
     * @param op                           操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(ConfigChannelChangeValidator configChannelChangeValidator, String op) {

        //记录日志
        log.info("[通道切换][{}]数据:{}", op, JSON.toJSONString(configChannelChangeValidator));
        if ("add".equals(op)) {
            configChannelChangeRepository.addChannelChange(configChannelChangeValidator);
            return ResponseDataUtil.buildSuccess();
        }
        if ("edit".equals(op)) {
            String originalAccountIds = "";
            List<ConfigChannelChangeItem> list = configChannelChangeItemRepository.findConfigChannelChangeItemByChangeIdAndStatus(configChannelChangeValidator.getId(), "1");
            if (null != list && list.size() > 0) {
                for (ConfigChannelChangeItem obj : list) {
                    if (StringUtils.isEmpty(originalAccountIds)) {
                        originalAccountIds = obj.getBusinessAccount();
                    } else {
                        originalAccountIds += "," + obj.getBusinessAccount();
                    }
                }
            }
            log.info("[update originalAccountIds]:{}",originalAccountIds);
            configChannelChangeRepository.editChannelChange(configChannelChangeValidator, originalAccountIds);
            return ResponseDataUtil.buildSuccess();
        }

        return ResponseDataUtil.buildError();
    }

    /**
     * 取消通道变更
     *
     * @param id
     */
    @Transactional
    public ResponseData cancelChannelChange(String id) {

        Optional<ConfigChannelChange> data = configChannelChangeRepository.findById(id);
        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }
        log.info("[通道切换][{}]数据:{}", "取消切换", JSON.toJSONString(data.get()));

        String originalAccountIds = "";
        List<ConfigChannelChangeItem> list = configChannelChangeItemRepository.findConfigChannelChangeItemByChangeIdAndStatus(data.get().getId(), "1");
        if (null != list && list.size() > 0) {
            for (ConfigChannelChangeItem obj : list) {
                if (StringUtils.isEmpty(originalAccountIds)) {
                    originalAccountIds = obj.getBusinessAccount();
                } else {
                    originalAccountIds += "," + obj.getBusinessAccount();
                }
            }
        }

        log.info("[cancel originalAccountIds]:{}",originalAccountIds);

        configChannelChangeRepository.cancelChannelChange(data.get(), originalAccountIds);
        return ResponseDataUtil.buildSuccess();
    }
}
