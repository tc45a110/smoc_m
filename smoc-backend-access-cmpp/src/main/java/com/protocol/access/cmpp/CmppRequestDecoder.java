package com.protocol.access.cmpp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.cmpp.pdu.CmppPDU;
import com.protocol.access.cmpp.pdu.CmppPDUParser;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.manager.SessionManager;

public class CmppRequestDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory
			.getLogger(CmppRequestDecoder.class);

	@Override
	protected boolean doDecode(final IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		if (in.remaining() > 4) {
			in.mark();
			int length = in.getInt();		
			logger.debug("length="+length);
			in.reset();
			if (length > in.remaining()){
				return false;
			}
			byte[] bytedata = new byte[length];
			in.get(bytedata);	
			ByteBuffer buffer = new ByteBuffer();
			buffer.appendBytes(bytedata);
			CmppPDU pdu = CmppPDUParser.createPDUFromBuffer(buffer,SessionManager.getInstance().getSessionVersion(session));
			if (pdu == null) return false;
			logger.debug(String.valueOf(pdu.header.getSequenceNumber()));
			out.write(pdu);
			return true;
		}

		return false;
	}
}
