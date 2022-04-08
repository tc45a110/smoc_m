package com.smoc.cloud.common.smoc.utils;


public class MessageUtil {

    public static final String MessageTaskStatus_wait = "01";//发送中
    public static final String MessageTaskStatus_finish = "02";//短信群发已完成
    public static final String MessageTaskStatus_stop = "03";//短信群发中断
    public static final String MessageTaskStatus_error = "04";//短信群发异常
    public static final String MessageTaskStatus_save = "05";//保存状态，可以修改删除，提交发送

}
