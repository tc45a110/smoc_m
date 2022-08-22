// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SMPPSubmitMessage.java

package com.huawei.insa2.comm.smpp.message;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.huawei.insa2.comm.smpp.SMPPConstant;

// Referenced classes of package com.huawei.insa2.comm.smpp.message:
//            SMPPMessage

public class SMPPSubmitMessage extends SMPPMessage
{
	public static final Logger logger = Logger.getLogger(SMPPSubmitMessage.class);

    private StringBuffer strBuf;

    public SMPPSubmitMessage(String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr, byte destAddrTon, byte destAddrNpi, String destinationAddr, 
            byte esmClass, byte protocolId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, byte registeredDelivery, byte replaceIfPresentFlag, 
            byte dataCoding, byte smDefaultMsgId, int smLength, byte[] shortMessage)
        throws IllegalArgumentException
    {
        if(serviceType.length() > 5)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":serviceType ").append(SMPPConstant.STRING_LENGTH_GREAT).append("5"))));
        if(sourceAddr.length() > 20)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":sourceAddr ").append(SMPPConstant.STRING_LENGTH_GREAT).append("20"))));
        if(destinationAddr == null)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":destinationAddr ").append(SMPPConstant.STRING_NULL))));
        if(destinationAddr.length() > 20)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":destinationAddr ").append(SMPPConstant.STRING_LENGTH_GREAT).append("20"))));
        if(scheduleDeliveryTime.length() != 0 && scheduleDeliveryTime.length() != 16)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":scheduleDeliveryTime ").append(SMPPConstant.STRING_LENGTH_NOTEQUAL).append("16"))));
        if(validityPeriod.length() != 0 && validityPeriod.length() != 16)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":validityPeriod ").append(SMPPConstant.STRING_LENGTH_NOTEQUAL).append("16"))));
        if(smLength > 254)
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":smLength ").append(SMPPConstant.STRING_LENGTH_GREAT).append("254"))));
        if(shortMessage == null)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMPPConstant.SUBMIT_INPUT_ERROR)))).append(":shortMessage ").append(SMPPConstant.STRING_LENGTH_GREAT).append("254"))));
        } else
        {
        	 logger.debug("serviceType:"+serviceType);
        	 logger.debug("sourceAddr:"+sourceAddr);
        	 logger.debug("destinationAddr:"+destinationAddr);
        	 logger.debug("scheduleDeliveryTime:"+scheduleDeliveryTime);
        	 logger.debug("validityPeriod:"+validityPeriod);
        	 logger.debug("smLength:"+smLength);
            int len = 33 + serviceType.length() + sourceAddr.length() + destinationAddr.length() + scheduleDeliveryTime.length() + validityPeriod.length() + shortMessage.length;
            logger.debug("len:"+len);
            buf = new byte[len];
            logger.debug("buf:"+Arrays.toString(buf));
            setMsgLength(len);
            logger.debug("buf:"+Arrays.toString(buf));
            setCommandId(4);
            logger.debug("buf:"+Arrays.toString(buf));
            setStatus(0);
            logger.debug("buf:"+Arrays.toString(buf));
            int pos = 16;
            System.arraycopy(serviceType.getBytes(), 0, buf, pos, serviceType.length());
            logger.debug("buf:"+Arrays.toString(buf));
            pos = pos + serviceType.length() + 1;
            buf[pos] = sourceAddrTon;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = sourceAddrNpi;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            System.arraycopy(sourceAddr.getBytes(), 0, buf, pos, sourceAddr.length());
            logger.debug("buf:"+Arrays.toString(buf));
            pos = pos + sourceAddr.length() + 1;
            buf[pos] = destAddrTon;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = destAddrNpi;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            System.arraycopy(destinationAddr.getBytes(), 0, buf, pos, destinationAddr.length());
            logger.debug("buf:"+Arrays.toString(buf));
            pos = pos + destinationAddr.length() + 1;
            buf[pos] = esmClass;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = protocolId;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = priorityFlag;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            System.arraycopy(scheduleDeliveryTime.getBytes(), 0, buf, pos, scheduleDeliveryTime.length());
            logger.debug("buf:"+Arrays.toString(buf));
            pos = pos + scheduleDeliveryTime.length() + 1;
            System.arraycopy(validityPeriod.getBytes(), 0, buf, pos, validityPeriod.length());
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = registeredDelivery;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = replaceIfPresentFlag;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = dataCoding;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = smDefaultMsgId;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            buf[pos] = (byte)smLength;
            logger.debug("buf:"+Arrays.toString(buf));
            pos++;
            System.arraycopy(shortMessage, 0, buf, pos, shortMessage.length);
            logger.debug("buf:"+Arrays.toString(buf));
            strBuf = new StringBuffer(600);
            strBuf.append(",serviceType=".concat(String.valueOf(String.valueOf(serviceType))));
            strBuf.append(",sourceAddrTon=".concat(String.valueOf(String.valueOf(sourceAddrTon))));
            strBuf.append(",sourceAddrNpi=".concat(String.valueOf(String.valueOf(sourceAddrNpi))));
            strBuf.append(",sourceAddr=".concat(String.valueOf(String.valueOf(sourceAddr))));
            strBuf.append(",destAddrTon=".concat(String.valueOf(String.valueOf(destAddrTon))));
            strBuf.append(",destAddrNpi=".concat(String.valueOf(String.valueOf(destAddrNpi))));
            strBuf.append(",destinationAddr=".concat(String.valueOf(String.valueOf(destinationAddr))));
            strBuf.append(",esmClass=".concat(String.valueOf(String.valueOf(esmClass))));
            strBuf.append(",protocolId=".concat(String.valueOf(String.valueOf(protocolId))));
            strBuf.append(",priorityFlag=".concat(String.valueOf(String.valueOf(priorityFlag))));
            strBuf.append(",scheduleDeliveryTime=".concat(String.valueOf(String.valueOf(scheduleDeliveryTime))));
            strBuf.append(",validityPeriod=".concat(String.valueOf(String.valueOf(validityPeriod))));
            strBuf.append(",registeredDelivery=".concat(String.valueOf(String.valueOf(registeredDelivery))));
            strBuf.append(",replaceIfPresentFlag=".concat(String.valueOf(String.valueOf(replaceIfPresentFlag))));
            strBuf.append(",dataCoding=".concat(String.valueOf(String.valueOf(dataCoding))));
            strBuf.append(",smDefaultMsgId=".concat(String.valueOf(String.valueOf(smDefaultMsgId))));
            strBuf.append(",smLength=".concat(String.valueOf(String.valueOf(smLength))));
            strBuf.append(",shortMessage=".concat(String.valueOf(String.valueOf(shortMessage))));
            return;
        }
    }

    public String toString()
    {
        StringBuffer outBuf = new StringBuffer(600);
        outBuf.append("SMPPSubmitMessage: ");
        outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
        outBuf.append(",CommandID=".concat(String.valueOf(String.valueOf(getCommandId()))));
        outBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
        outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        if(strBuf != null)
            outBuf.append(strBuf.toString());
        return outBuf.toString();
    }
}
