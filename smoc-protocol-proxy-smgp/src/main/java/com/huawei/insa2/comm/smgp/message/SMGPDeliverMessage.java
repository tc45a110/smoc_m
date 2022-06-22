// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SMGPDeliverMessage.java

package com.huawei.insa2.comm.smgp.message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huawei.insa2.comm.smgp.SMGPConstant;
import com.huawei.insa2.util.TypeConvert;

// Referenced classes of package com.huawei.insa2.comm.smgp.message:
//            SMGPMessage

public class SMGPDeliverMessage extends SMGPMessage
{

    public SMGPDeliverMessage(byte buf[])
        throws IllegalArgumentException
    {
    	//buf包含SequenceID,有的消息包含可选字节
        int len = 81 + (buf[72] & 0xff);
        if(buf.length < len)
        {
            throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            super.buf = new byte[buf.length];
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public byte[] getMsgId()
    {
    	byte[] msgId = new byte[10];
		System.arraycopy(getMsgContent(), 3, msgId, 0, msgId.length);
        return msgId;
    }

    public int getIsReport()
    {
        return buf[14];
    }

    public int getMsgFormat()
    {
        return buf[15];
    }

    public Date getRecvTime()
    {
        Date date;
        try
        {
            byte tmpbyte[] = new byte[4];
            System.arraycopy(buf, 16, tmpbyte, 0, 4);
            String tmpstr = new String(tmpbyte);
            int tmpYear = Integer.parseInt(tmpstr);
            tmpbyte = new byte[2];
            System.arraycopy(buf, 20, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpMonth = Integer.parseInt(tmpstr) - 1;
            System.arraycopy(buf, 22, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpDay = Integer.parseInt(tmpstr);
            System.arraycopy(buf, 24, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpHour = Integer.parseInt(tmpstr);
            System.arraycopy(buf, 26, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpMinute = Integer.parseInt(tmpstr);
            System.arraycopy(buf, 28, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpSecond = Integer.parseInt(tmpstr);
            Calendar calendar = Calendar.getInstance();
            calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute, tmpSecond);
            Date date1 = calendar.getTime();
            return date1;
        }
        catch(Exception e)
        {
            date = null;
        }
        return date;
    }

    public String getSrcTermID()
    {
        byte srcTermId[] = new byte[21];
        System.arraycopy(buf, 30, srcTermId, 0, 21);
        return (new String(srcTermId)).trim();
    }

    public String getDestTermID()
    {
        byte destTermId[] = new byte[21];
        System.arraycopy(buf, 51, destTermId, 0, 21);
        return (new String(destTermId)).trim();
    }

    public int getMsgLength()
    {
        return buf[72] & 0xff;
    }

    public byte[] getMsgContent()
    {
        int len = buf[72] & 0xff;
        byte content[] = new byte[len];
        System.arraycopy(buf, 73, content, 0, len);
        return content;
    }

    public String getReserve()
    {
        int loc = 73 + (buf[72] & 0xff);
        byte reserve[] = new byte[8];
        System.arraycopy(buf, loc, reserve, 0, 8);
        return (new String(reserve)).trim();
    }
    
    public String getStat() {
    	byte[] stat_b = new byte[7];
		System.arraycopy(getMsgContent(), 81, stat_b, 0, stat_b.length);
    	return new String(stat_b);
    }
    
    public String getErr() {
    	byte[] err = new byte[3];
		System.arraycopy(getMsgContent(), 93, err, 0, err.length);
    	return new String(err);
    }

    public String toString()
    {
        StringBuffer strBuf = new StringBuffer(600);
        strBuf.append("SMGPDeliverMessage: ");
        strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        strBuf.append(",MsgID=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
        strBuf.append(",IsReport=".concat(String.valueOf(String.valueOf(getIsReport()))));
        strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(getMsgFormat()))));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if(getRecvTime() != null)
            strBuf.append(",RecvTime=".concat(String.valueOf(String.valueOf(dateFormat.format(getRecvTime())))));
        else
            strBuf.append(",RecvTime=null");
        strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(getSrcTermID()))));
        strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(getDestTermID()))));
        strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
        strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(new String(getMsgContent())))));
        strBuf.append(",reserve=".concat(String.valueOf(String.valueOf(getReserve()))));
        return strBuf.toString();
    }

    public int getRequestId()
    {
        return 3;
    }
}
