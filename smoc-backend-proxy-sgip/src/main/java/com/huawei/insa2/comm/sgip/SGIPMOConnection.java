// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-16 19:21:43
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SGIPConnection.java

package com.huawei.insa2.comm.sgip;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PWriter;
import com.huawei.smproxy.SGIPMOProxy;

public class SGIPMOConnection extends SGIPMOSocketConnection
{
	public SGIPMOConnection(SGIPMOProxy proxy) {
		super.proxy = proxy;
	}

	protected PWriter getWriter(OutputStream out) {
		return new SGIPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new SGIPReader(in);
	}
	
	public void attach(Socket socket){
		init(socket);
	}
}