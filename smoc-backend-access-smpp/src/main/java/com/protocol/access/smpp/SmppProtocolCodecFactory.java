package com.protocol.access.smpp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * TODO: Document me
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 *
 */
public class SmppProtocolCodecFactory implements ProtocolCodecFactory {
  private ProtocolDecoder decoder = new SmppRequestDecoder();
  private ProtocolEncoder encoder = new SmppResponseEncoder();

  public ProtocolDecoder getDecoder(IoSession sessionIn) throws Exception {
    return decoder;
  }

  public ProtocolEncoder getEncoder(IoSession sessionIn) throws Exception {
    return encoder;
  }
}
