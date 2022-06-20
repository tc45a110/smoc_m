// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 17:43:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CMPPWriter.java

package com.huawei.insa2.comm.cmpp;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.PMessage;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;

public class CMPPWriter extends PWriter {
	private static Logger logger = Logger.getLogger(CMPPWriter.class);
	public CMPPWriter(OutputStream out) {
		this.out = out;
	}

	public void write(PMessage message) throws IOException {
		CMPPMessage msg = (CMPPMessage) message;
		logger.debug("start write sequence="+msg.getSequenceId());
		out.write(msg.getBytes());
		logger.debug("end write sequence="+msg.getSequenceId());
	}

	public void writeHeartbeat() throws IOException {
	}

	protected OutputStream out;
}