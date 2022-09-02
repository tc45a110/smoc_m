package com.base.common.manager;

import com.base.common.worker.SuperQueueWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class RocketMQManager extends SuperQueueWorker<String> {

    private static final String PRODUCER_GROUP = "smoc_base_backend_group_name";

    private static final String NAME_SERVER_ADDR = "172.28.235.188:9876";

    private static final String TOPIC = "TopicTest";

    private static final String TAGS = "TagA";

    private static final int RETRY_TIMES_WHEN_SEND_FAILED = 2;

    private static final int RETRY_TIMES_WHEN_ASYNC_SEND_FAILED = 2;

    private static final int SEND_MESSAGE_TIMEOUT = 30000;

    private static final int MAX_MESSAGE_SIZE = 4096;

    private static final int COMPRESS_MESSAGE_BODY_THRESHOLD = 4096;

    private long lastActiveTime = 0l;

    private boolean activeFlag = false;

    private DefaultMQProducer producer;

    public RocketMQManager(){
        try{
            initialize();
            this.start();
        }catch (Exception e){
            logger.error("启动失败");
            this.exit();
        }
    }

    @Override
    protected void doRun() throws Exception {
        String message = this.poll();
        if(StringUtils.isNotEmpty(message)){
            //Producer已关闭 启动Producer
            if(!activeFlag){
                initialize();
            }
            sendMessage(message);
            lastActiveTime = System.currentTimeMillis();
        }else{
            long stopTime = System.currentTimeMillis() - lastActiveTime;
            if(lastActiveTime != 0 && stopTime > INTERVAL){
                // 一分钟无需发送消息，关闭Producer实例。
                producer.shutdown();
                activeFlag = false;
            }
        }
    }

    private void initialize() throws Exception {
        // 实例化消息生产者Producer
        producer = new DefaultMQProducer(PRODUCER_GROUP);
        // 设置NameServer的地址
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        // 设置消息同步发送失败时的重试次数，默认为 2
        producer.setRetryTimesWhenSendFailed(RETRY_TIMES_WHEN_SEND_FAILED);
        // 设置消息发送超时时间，默认3000ms
        producer.setSendMsgTimeout(SEND_MESSAGE_TIMEOUT);
        // 最大的消息限制 默认为128K
        producer.setMaxMessageSize(MAX_MESSAGE_SIZE);
        // 消息达到4096字节的时候，消息就会被压缩。默认4096
        producer.setCompressMsgBodyOverHowmuch(COMPRESS_MESSAGE_BODY_THRESHOLD);
        // 启动Producer实例
        producer.start();
        activeFlag = true;
        lastActiveTime = System.currentTimeMillis();
    }

    private void sendMessage(String message) throws Exception {
        Message msg = new Message(TOPIC,
                TAGS,
                (message).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        // 发送消息到一个Broker
        SendResult sendResult = producer.send(msg);
        // 通过sendResult返回消息是否成功送达
        logger.info(sendResult.toString());
    }

}
