package com.smoc.cloud.scheduler.configuration;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 获取系统配置
 */
@Component
public class SystemConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getPort() {

        return this.serverPort;
    }

    /**
     * 获取系统ip
     *
     * @return
     */
    public String getIp() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress() + ":" + this.getPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknownIp:" + this.getPort();
    }
}
