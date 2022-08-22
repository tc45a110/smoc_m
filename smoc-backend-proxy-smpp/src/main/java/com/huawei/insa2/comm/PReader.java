package com.huawei.insa2.comm;

import java.io.IOException;

import com.huawei.insa2.comm.smpp.message.SMPPMessage;

/**
 * 协议解码器的父类。
 * @author 李大伟
 * @version 1.0
 */
public abstract class PReader {

  /**
   * 向编码器写入一个消息。
   * @param message 待写入的消息。
   */
  public abstract SMPPMessage read() throws IOException;
}