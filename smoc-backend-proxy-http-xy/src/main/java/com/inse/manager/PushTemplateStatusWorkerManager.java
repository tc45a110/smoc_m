package com.inse.manager;
import com.base.common.worker.SuperMapWorker;
import com.inse.worker.PushTemplateStatusWorker;
import java.util.HashSet;
import java.util.Set;

public class PushTemplateStatusWorkerManager extends SuperMapWorker<String, PushTemplateStatusWorker> {

    private static PushTemplateStatusWorkerManager manager = new PushTemplateStatusWorkerManager();

    private PushTemplateStatusWorkerManager() {
        this.setName("PushTemplateStatusWorkerManager");
        this.start();
    }

    public static PushTemplateStatusWorkerManager getInstance() {
        return manager;
    }

    public void doRun() throws Exception {
        sleep(INTERVAL);
    }

    public void maintain(String channelID, PushTemplateStatusWorker pushTemplateStatusWorker) {
        add(channelID, pushTemplateStatusWorker);
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
    public void exit(String channelID) {
        PushTemplateStatusWorker pushTemplateStatusWorker = remove(channelID);
        if (pushTemplateStatusWorker != null) {
            pushTemplateStatusWorker.exit();
        }
    }
}
