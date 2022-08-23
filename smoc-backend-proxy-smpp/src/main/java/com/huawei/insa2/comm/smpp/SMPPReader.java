// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SMPPReader.java

package com.huawei.insa2.comm.smpp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkMessage;
import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkRespMessage;
import com.huawei.insa2.comm.smpp.message.SMPPLoginRespMessage;
import com.huawei.insa2.comm.smpp.message.SMPPMessage;
import com.huawei.insa2.comm.smpp.message.SMPPSubmitRespMessage;
import com.huawei.insa2.comm.smpp.message.SMPPUnbindMessage;
import com.huawei.insa2.comm.smpp.message.SMPPUnbindRespMessage;
import com.huawei.insa2.util.TypeConvert;

public class SMPPReader extends PReader
{
	private static Logger logger = Logger.getLogger(SMPPReader.class);
	
    protected DataInputStream in;

    public SMPPReader(InputStream is)
    {
        in = new DataInputStream(is);
    }

    public SMPPMessage read()
        throws IOException
    {
        int total_Length = in.readInt();
        if(total_Length > 1024*4){
        	logger.warn("读取的数据长度太大："+total_Length);
        	throw new IOException("too large");
        }
        int command_Id = in.readInt();
        byte tmpbuf[] = new byte[total_Length - 8];
        in.readFully(tmpbuf);
        logger.debug(Arrays.toString(tmpbuf));
        byte buf[] = new byte[total_Length];
        System.arraycopy(tmpbuf, 0, buf, 8, tmpbuf.length);
        TypeConvert.int2byte(total_Length, buf, 0);
        TypeConvert.int2byte(command_Id, buf, 4);
        if(command_Id == 0x80000001 || command_Id == 0x80000002 || command_Id == 0x80000009)
            return new SMPPLoginRespMessage(buf);
        if(command_Id == 5)
            return new SMPPDeliverMessage(buf);
        if(command_Id == 0x80000004)
            return new SMPPSubmitRespMessage(buf);
        if(command_Id == 21)
            return new SMPPEnquireLinkMessage(buf);
        if(command_Id == 0x80000015)
            return new SMPPEnquireLinkRespMessage(buf);
        if(command_Id == 6)
            return new SMPPUnbindMessage(buf);
        if(command_Id == 0x80000006)
            return new SMPPUnbindRespMessage(buf);
        else
            return null;
    }
}
