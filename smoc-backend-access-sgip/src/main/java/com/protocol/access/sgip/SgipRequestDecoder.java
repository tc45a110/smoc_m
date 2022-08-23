package com.protocol.access.sgip;


import com.protocol.access.sgip.pdu.SgipPDU;
import com.protocol.access.sgip.pdu.SgipPDUParser;
import com.protocol.access.sgip.sms.ByteBuffer;

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
public class SgipRequestDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory
			.getLogger(SgipRequestDecoder.class);

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
			SgipPDU pdu = SgipPDUParser.createPDUFromBuffer(buffer);
			if (pdu == null) return false;
			logger.debug(String.valueOf(pdu.header.getSequenceNumber()));
			out.write(pdu);
			return true;

		}

		return false;
	}
}
