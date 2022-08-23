package com.protocol.access.smgp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * TODO: Document me
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 *
 */
public class SmgpProtocolCodecFactory implements ProtocolCodecFactory {
  private ProtocolDecoder decoder = new SmgpRequestDecoder();
  private ProtocolEncoder encoder = new SmgpResponseEncoder();

  public ProtocolDecoder getDecoder(IoSession sessionIn) throws Exception {
    return decoder;
  }

  public ProtocolEncoder getEncoder(IoSession sessionIn) throws Exception {
    return encoder;
  }
}
