package com.smoc.cloud.configure.channel.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.configure.channel.remote.ConfigChannelChangeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 通道切换
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelChangeService {

    @Autowired
    private ConfigChannelChangeFeignClient configChannelChangeFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigChannelChangeValidator>> page(PageParams<ConfigChannelChangeValidator> pageParams) {
        try {
            ResponseData responseData = this.configChannelChangeFeignClient.page(pageParams);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<ConfigChannelChangeValidator> findById(String id) {
        try {
            ResponseData<ConfigChannelChangeValidator> data = this.configChannelChangeFeignClient.findById(id);
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
    public ResponseData save(ConfigChannelChangeValidator configChannelChangeValidator, String op) {

        try {
            ResponseData data = this.configChannelChangeFeignClient.save(configChannelChangeValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 取消变更,所有的业务账号都恢复到正常状态
     *
     * @param id
     * @return
     */
    public ResponseData cancelChannelChange(String id) {
        try {
            ResponseData data = this.configChannelChangeFeignClient.cancelChannelChange(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


}
