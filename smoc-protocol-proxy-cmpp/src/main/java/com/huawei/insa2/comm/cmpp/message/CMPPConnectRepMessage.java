// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 17:43:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CMPPConnectRepMessage.java

package com.huawei.insa2.comm.cmpp.message;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.cmpp.CMPPConstant;
import com.huawei.insa2.util.TypeConvert;

// Referenced classes of package com.huawei.insa2.comm.cmpp.message:
//            CMPPMessage

public class CMPPConnectRepMessage extends CMPPMessage {
	private static Logger logger = Logger
			.getLogger(CMPPConnectRepMessage.class);

	public CMPPConnectRepMessage(byte buf[]) throws IllegalArgumentException {
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

	public byte getStatus() {
		if (buf.length == 22) {
			return super.buf[4];
		}
		return super.buf[7];

	}

	public byte[] getAuthenticatorISMG() {
		byte[] tmpbuf = new byte[16];
		if (buf.length == 22) {
			System.arraycopy(super.buf, 5, tmpbuf, 0, 16);
		} else {
			System.arraycopy(buf, 8, tmpbuf, 0, 16);
		}
		return tmpbuf;
	}

	public byte getVersion() {
		if (buf.length == 22) {
			return super.buf[21];
		}
		return super.buf[24];
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