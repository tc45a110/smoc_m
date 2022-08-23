// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   SMPPConnection.java

package com.huawei.insa2.comm.smpp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PSocketConnection;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.smpp.message.SMPPMessage;
import com.huawei.smproxy.Proxy;

// Referenced classes of package com.huawei.insa2.comm.smpp:
//            SMPPWriter, SMPPReader, SMPPTransaction, SMPPConstant

public class SMPPConnection extends PSocketConnection
{
	public SMPPConnection(String channelID,String index,Proxy proxy,BlockingQueue<SMPPMessage> sendSubmitRespQueue) {
		super.proxy = proxy;
		super.channelID = channelID;
		super.index = index;
		init(sendSubmitRespQueue);
	}
	
	public boolean isHealth(){
		return health;
	}

	protected PWriter getWriter(OutputStream out) {
		return new SMPPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new SMPPReader(in);
	}
}
