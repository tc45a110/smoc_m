package com.protocol.access.smgp;


import com.protocol.access.manager.SessionManager;
import com.protocol.access.smgp.pdu.SmgpPDU;
import com.protocol.access.smgp.pdu.SmgpPDUParser;
import com.protocol.access.smgp.sms.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SmgpRequestDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory
			.getLogger(SmgpRequestDecoder.class);

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
			SmgpPDU pdu = SmgpPDUParser.createPDUFromBuffer(buffer,SessionManager.getInstance().getSessionVersion(session));
			if (pdu == null) return false;
			logger.debug(String.valueOf(pdu.header.getSequenceID()));
			out.write(pdu);
			return true;

		}

		return false;
	}
}
