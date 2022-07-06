// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   CMPP30ConnectRepMessage.java

package com.huawei.insa2.comm.cmpp30.message;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.cmpp.CMPPConstant;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.util.TypeConvert;

public class CMPP30ConnectRepMessage extends CMPPMessage {
	private static Logger logger = Logger
			.getLogger(CMPP30ConnectRepMessage.class);

	public CMPP30ConnectRepMessage(byte buf[]) throws IllegalArgumentException {
		super.buf = new byte[buf.length];
		logger.debug("super.buf.length=" + super.buf.length + "{"
				+ Arrays.toString(buf) + "}");
		if (buf.length == 22 || buf.length == 25) {
			System.arraycopy(buf, 0, super.buf, 0, buf.length);
			super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
			return;
		}
		throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
	}

	public int getStatus() {
		if (buf.length == 25) {
			return TypeConvert.byte2int(buf, 4);
		}
		return super.buf[4];
	}

	public byte[] getAuthenticatorISMG() {
		byte[] tmpbuf = new byte[16];
		if (buf.length == 25) {
			System.arraycopy(buf, 8, tmpbuf, 0, 16);
		} else {
			System.arraycopy(super.buf, 5, tmpbuf, 0, 16);
		}
		return tmpbuf;
	}

	public byte getVersion() {
		if (buf.length == 25) {
			return super.buf[24];
		}
		return super.buf[21];
	}

	public String toString() {
		String tmpStr = "CMPP_Connect_REP: ";
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String
				.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=")
				.append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String
				.valueOf(String.valueOf(tmpStr)))).append(",Status=").append(
				getStatus())));
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String
				.valueOf(String.valueOf(tmpStr))))
				.append(",AuthenticatorISMG=").append(
						new String(Arrays.toString(getAuthenticatorISMG())))));
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String
				.valueOf(String.valueOf(tmpStr)))).append(",Version=").append(
				getVersion())));
		return tmpStr;
	}

	public int getCommandId() {
		return 0x80000001;
	}
}
