// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   SMGPConnection.java

package com.huawei.insa2.comm.smgp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PSocketConnection;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.smgp.message.SMGPMessage;
import com.huawei.smproxy.SMProxy;

//Referenced classes of package com.huawei.insa2.comm.smgp:
//SMGPWriter, SMGPReader, SMGPTransaction, SMGPConstant
/**
* PSMGP协议的连接层。
*/
public class SMGPConnection extends PSocketConnection
{
	public SMGPConnection(String channelID,String index,SMProxy SMProxy,BlockingQueue<SMGPMessage> sendSubmitRespQueue) {
		super.proxy = SMProxy;
		super.channelID = channelID;
		super.index = index;
		init(sendSubmitRespQueue);
	}
	
	public boolean isHealth(){
		return health;
	}

	protected PWriter getWriter(OutputStream out) {
		return new SMGPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new SMGPReader(in);
	}
}
