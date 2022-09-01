package com.inse.manager;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperMapWorker;
import com.inse.worker.PushTemplateStatusWorker;
import org.apache.commons.collections4.CollectionUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubmitPullWorkerManager extends SuperMapWorker<String, Set<SubmitPullWorker>> {

    private static SubmitPullWorkerManager manager = new SubmitPullWorkerManager();

    private SubmitPullWorkerManager() {
        this.setName("SubmitPullWorkerManager");
        this.start();
    }

    public static SubmitPullWorkerManager getInstance() {
        return manager;
    }

    public void doRun() throws Exception {

        String protocol = ResourceManager.getInstance().getValue("load.channel.protocol");
        String channelIDs = ResourceManager.getInstance().getValue("load.channel.id");
        // 获取可加载的通道ID
        Set<String> availableChannelIDSet = ChannelInfoManager.getInstance().getAvailableChannelIDs(protocol,
                channelIDs);
        // 获取已启动的通道ID
        Set<String> startedChannelIDSet = new HashSet<String>(keySet());
        // 双向求差，分别获取到需新启动和需要停止的通道
        Collection<String> startChannelIDCollection = CollectionUtils.removeAll(availableChannelIDSet,
                startedChannelIDSet);
        if (startChannelIDCollection.size() > 0) {
            logger.info("需要启动的通道{}", startChannelIDCollection);
        }

        Collection<String> stopChannelIDCollection = CollectionUtils.removeAll(startedChannelIDSet,
                availableChannelIDSet);
        if (stopChannelIDCollection.size() > 0) {
            logger.info("需要停止的通道{}", stopChannelIDCollection);
        }

        start(startChannelIDCollection);
        exit(stopChannelIDCollection);
        sleep(INTERVAL);
    }

    /**
     * 停止
     *
     * @param channelIDCollection
     */
    private void exit(Collection<String> channelIDCollection) {
        for (String channelID : channelIDCollection) {
            exit(channelID);
        }
    }

    /**
     * 启用
     *
     * @param channelIDCollection
     */
    private void start(Collection<String> channelIDCollection) {

        for (String channelID : channelIDCollection) {

            // 获取通道接口扩展参数
            Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
            String port = resultMap.get("localPort");

            // 启动回调接收服务
            CallbackHTTPServer callbackHTTPServer = new CallbackHTTPServer(port, channelID);
            callbackHTTPServer.start();
            CallbackHTTPServerManager.getInstance().maintain(channelID,callbackHTTPServer);

            //启动模板推送服务
            MateriaMessageWorker materiaMessageworker = new MateriaMessageWorker(channelID);
            materiaMessageworker.setName("MateriaMessageWorker-"+channelID);
            materiaMessageworker.start();
            MateriaMessageWorkerManager.getInstance().maintain(channelID,materiaMessageworker);

            //启动模板状态拉取服务
            PushTemplateStatusWorker pushTemplateStatusWorkerManager=new PushTemplateStatusWorker(channelID);
            pushTemplateStatusWorkerManager.setName("PushTemplateStatusWorkerManager-"+channelID);
            pushTemplateStatusWorkerManager.start();
            PushTemplateStatusWorkerManager.getInstance().maintain(channelID,pushTemplateStatusWorkerManager);
            // 获取连接数
            int connectNumber = ChannelInfoManager.getInstance().getConnectNumber(channelID);
            Set<SubmitPullWorker> submitPullWorkerSet = new HashSet<SubmitPullWorker>(connectNumber);

            // 根据连接数来启动线程
            for (int i = 1; i <= connectNumber; i++) {
                SubmitPullWorker submitPullWorker = new SubmitPullWorker(channelID, String.valueOf(i));
                submitPullWorkerSet.add(submitPullWorker);
            }

            add(channelID, submitPullWorkerSet);

        }
    }

    /**
     * 退出线程
     */
    public void exit() {
        super.exit();
        Set<String> channelIDSet = new HashSet<String>(keySet());
        for (String channelID : channelIDSet) {
            exit(channelID);
        }
    }

    /**
     * 将某个通道的线程停止
     *
     * @param channelID
     */
    private void exit(String channelID) {
        Set<SubmitPullWorker> submitPullWorkerSet = remove(channelID);
        if (submitPullWorkerSet != null) {
            for (SubmitPullWorker submitPullWorker : submitPullWorkerSet) {
                submitPullWorker.exit();
            }
        }
        MateriaMessageWorkerManager.getInstance().exit(channelID);
        CallbackHTTPServerManager.getInstance().exit(channelID);
        PushTemplateStatusWorkerManager.getInstance().exit(channelID);
    }

}
