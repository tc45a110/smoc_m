// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-16 19:21:43
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SGIPConnection.java

package com.huawei.insa2.comm.sgip;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;

public class SGIPConnection extends PSocketConnection
{
	public SGIPConnection(String channelID,String index,Proxy proxy,BlockingQueue<SGIPMessage> sendSubmitRespQueue) {
		super.proxy = proxy;
		super.channelID = channelID;
		super.index = index;
		init(sendSubmitRespQueue);
	}
	
	public SGIPConnection(Proxy proxy) {
		super.proxy = proxy;
	}
	
	public boolean isHealth(){
		return health;
	}

	protected PWriter getWriter(OutputStream out) {
		return new SGIPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new SGIPReader(in);
	}
}