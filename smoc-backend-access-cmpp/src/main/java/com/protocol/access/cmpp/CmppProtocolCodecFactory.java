package com.protocol.access.cmpp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CmppProtocolCodecFactory implements ProtocolCodecFactory {
  private ProtocolDecoder decoder = new CmppRequestDecoder();
  private ProtocolEncoder encoder = new CmppResponseEncoder();

  public ProtocolDecoder getDecoder(IoSession sessionIn) throws Exception {
    return decoder;
  }

  public ProtocolEncoder getEncoder(IoSession sessionIn) throws Exception {
    return encoder;
  }
}
