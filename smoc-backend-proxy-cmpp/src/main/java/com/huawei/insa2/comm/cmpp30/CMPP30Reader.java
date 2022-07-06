// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   CMPP30Reader.java

package com.huawei.insa2.comm.cmpp30;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPCancelRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPQueryRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPTerminateMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPTerminateRepMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30ConnectRepMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage;

public class CMPP30Reader extends PReader {
	private static Logger logger = Logger.getLogger(CMPP30Reader.class);

	protected DataInputStream in;

	public CMPP30Reader(InputStream is) {
		in = new DataInputStream(is);
	}

	public CMPPMessage read() throws IOException {
		int total_Length = in.readInt();
		int command_Id = in.readInt();
		byte buf[] = new byte[total_Length - 8];
		in.readFully(buf);
		if (command_Id == 0x80000001)
			return new CMPP30ConnectRepMessage(buf);
		if (command_Id == 5)
			return new CMPP30DeliverMessage(buf);
		if (command_Id == 0x80000004)
			return new CMPP30SubmitRepMessage(buf);
		if (command_Id == 0x80000006)
			return new CMPPQueryRepMessage(buf);
		if (command_Id == 0x80000007)
			return new CMPPCancelRepMessage(buf);
		if (command_Id == 0x80000008) {
			logger.debug("3.0-Active_Test_Rep:" + Arrays.toString(buf));
			return new CMPPActiveRepMessage(buf);
		}

		if (command_Id == 8)
			return new CMPPActiveMessage(buf);
		if (command_Id == 2)
			return new CMPPTerminateMessage(buf);
		if (command_Id == 0x80000002)
			return new CMPPTerminateRepMessage(buf);
		else
			return null;
	}
}
