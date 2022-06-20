// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 17:12:57
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) annotate_fullnames
// Source File Name:   CMPP30Connection.java

package com.huawei.insa2.comm.cmpp30;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PSocketConnection;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.cmpp.CMPPWriter;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.smproxy.SMProxy30;

// Referenced classes of package com.huawei.insa2.comm.cmpp30:
//            CMPP30Reader, CMPP30Transaction
/**
 * PCMPP30协议的连接层。
 */
public class CMPP30Connection extends PSocketConnection {
	
	private SMProxy30 SMProxy;

	public CMPP30Connection(String channelID,String index,SMProxy30 SMProxy,BlockingQueue<CMPPMessage> sendSubmitRespQueue) {
		super.proxy = SMProxy;
		super.channelID = channelID;
		super.index = index;
		init(sendSubmitRespQueue);
	}
	
	public boolean isHealth(){
		return health;
	}

	protected PWriter getWriter(OutputStream out) {
		return new CMPPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new CMPP30Reader(in);
	}

	public int getTransactionTimeout() {
		return super.transactionTimeout;
	}

	public SMProxy30 getSMProxy() {
		return SMProxy;
	}
	
}