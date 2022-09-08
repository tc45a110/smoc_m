package com.protocol.proxy.server;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.protocol.proxy.manager.AccountChanelTemplateInfoManager;
import com.protocol.proxy.manager.ChannelInteractiveStatusManager;
import com.protocol.proxy.manager.ReportWorkerManager;
import com.protocol.proxy.manager.SubmitPullWorkerManager;

public class ProxyServer {

    public static void main(String[] args) {
        try {
            ProxyServer proxyServer = new ProxyServer();
            proxyServer.startup();

            EchoServer echoServer = new EchoServer(proxyServer);
            echoServer.startup();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务启动失败");
            System.exit(0);
        }
    }

    /**
     * 启动服务
     */
    private void startup() {

        System.out.println("服务正在启动");
        // 初始化文件配置数据
        ResourceManager.getInstance();
        // 初始化数据库配置数据
        ExtendParameterManager.getInstance();
        // 通道信息
        ChannelInfoManager.getInstance();
        // 通道模板信息
        AccountChanelTemplateInfoManager.getInstance();
        // 通道拉取服务
        SubmitPullWorkerManager.getInstance();
        System.out.println("服务已启动");

    }

    /**
     * 停止服务
     */
    public void stop() {
        try {
            SubmitPullWorkerManager.getInstance().exit();
            int sleepTime = ResourceManager.getInstance().getIntValue("shutdown.sleep.time");
            if (sleepTime == 0) {
                sleepTime = 5;
            }
            ReportWorkerManager.getInstance().exit();
            ChannelInteractiveStatusManager.getInstance().exit();
            // 默认5秒之后 关闭服务 保证足够的时间处理内存中的数据
            Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME * sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务已停止");
        System.exit(0);
    }

}
