package com.protocol.access.smpp;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.manager.SessionManager;
import com.protocol.access.smpp.pdu.SmppPDU;
import com.protocol.access.smpp.pdu.SmppPDUParser;
import com.protocol.access.smpp.sms.ByteBuffer;



/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SmppRequestDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory
			.getLogger(SmppRequestDecoder.class);

	@Override
	protected boolean doDecode(final IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		if (in.remaining() > 4) {
			in.mark();
			int length = in.getInt();		
			logger.debug("length="+length);
			in.reset();
			if (length > in.remaining())
			{
				return false;
			}

			byte[] bytedata = new byte[length];
			in.get(bytedata);	
			ByteBuffer buffer = new ByteBuffer();
//			buffer.appendInt(length);
			buffer.appendBytes(bytedata);
			SmppPDU pdu = SmppPDUParser.createPDUFromBuffer(buffer,SessionManager.getInstance().getSessionVersion(session));
			if (pdu == null) return false;
			logger.debug(String.valueOf(pdu.header.getSequenceNo()));
			out.write(pdu);
			return true;

		}

		return false;
	}
}
