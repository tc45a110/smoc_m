package com.smoc.cloud.tablestore.utils;


import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.AlwaysRetryStrategy;
import com.smoc.cloud.tablestore.properties.TableStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TableStoreUtils {

    @Autowired
    private TableStoreProperties tableStoreProperties;

    /**
     * 初始化
     *
     * @return
     */
    public SyncClient client() {

        // ClientConfiguration提供了很多配置项，以下只列举部分。
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // 设置建立连接的超时时间。单位为毫秒。
        clientConfiguration.setConnectionTimeoutInMillisecond(5000);
        // 设置socket超时时间。单位为毫秒。
        clientConfiguration.setSocketTimeoutInMillisecond(5000);
        // 设置重试策略。如果不设置，则采用默认的重试策略。
        clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());
        SyncClient client = new SyncClient(tableStoreProperties.getEndPoint(), tableStoreProperties.getAccessKeyId(), tableStoreProperties.getAccessKeySecret(), tableStoreProperties.getInstanceName(), clientConfiguration);

        return client;
    }


}
