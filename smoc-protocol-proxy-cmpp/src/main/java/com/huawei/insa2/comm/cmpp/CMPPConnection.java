// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 17:43:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   CMPPConnection.java

package com.huawei.insa2.comm.cmpp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PSocketConnection;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.smproxy.SMProxy;

// Referenced classes of package com.huawei.insa2.comm.cmpp:
//            CMPPWriter, CMPPReader, CMPPTransaction, CMPPConstant
/**
 * PCMPP协议的连接层。
 */
public class CMPPConnection extends PSocketConnection {

	public CMPPConnection(String channelID,String index,SMProxy SMProxy,BlockingQueue<CMPPMessage> sendSubmitRespQueue) {
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
		return new CMPPReader(in);
	}

}