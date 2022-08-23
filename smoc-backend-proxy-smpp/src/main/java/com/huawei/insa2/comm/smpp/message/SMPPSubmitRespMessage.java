// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SMPPSubmitRespMessage.java

package com.huawei.insa2.comm.smpp.message;

import java.util.Arrays;

import org.apache.log4j.Logger;


// Referenced classes of package com.huawei.insa2.comm.smpp.message:
//            SMPPMessage

public class SMPPSubmitRespMessage extends SMPPMessage
{
	public static final Logger logger = Logger.getLogger(SMPPSubmitRespMessage.class);

    public SMPPSubmitRespMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[buf.length];
        System.arraycopy(buf, 0, super.buf, 0, buf.length);
    }

    public byte[] getMessageId()
    {
        return getFirstStr(buf, 16);
    }

    private byte[] getFirstStr(byte buf[], int sPos)
    {
        int length = 0;
 
        int pos;
        for(pos = sPos; pos < buf.length; pos++){
        	//byte=0时,退出
        	 if(buf[pos] == 0){
        		 break;
        	 }
        	 length++;
        }
        logger.debug("length:"+length);
        byte tmpBuf[] = new byte[length];
        
        System.arraycopy(buf, sPos, tmpBuf, 0, length);
        logger.debug("msgid:"+Arrays.toString(tmpBuf));
        
        return tmpBuf;
    }

    public String toString()
    {
        StringBuffer strBuf = new StringBuffer(200);
        strBuf.append("SMPPSubmitRespMessage: ");
        strBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
        strBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(getCommandId()))));
        strBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
        strBuf.append(",SequenceId=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        strBuf.append(",MessageId=".concat(String.valueOf(String.valueOf(new String(getMessageId())))));
        return strBuf.toString();
    }
}
