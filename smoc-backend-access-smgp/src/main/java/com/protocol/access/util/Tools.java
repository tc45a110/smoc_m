package com.protocol.access.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import com.base.common.manager.ResourceManager;
import com.protocol.access.smgp.util.SequenceUtil;




public class Tools {
	private static Logger logger = Logger.getLogger(Tools.class);
	private static int msgi = 1000;
	private static Object obj = new Object();
	private static Object obj2 = new Object();
	private static int gateway = 10 + (int)(Math.random()*90);
	static{
		//指定网关ID，避免多节点msgid重复
		if(ResourceManager.getInstance().getIntValue("gateway.id") > 0){
			gateway = ResourceManager.getInstance().getIntValue("gateway.id");
		}
	}
	private static int sequence = 1;

	public static byte[] GetMsgid() {
		String s_msg = "";
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		byte[] msgid = null;
		try {
			synchronized (obj) {
				msgi = msgi + 1;
			}
			if (msgi >= 9999)
				msgi = 1000;
			s_msg = time.format(nowTime) + msgi;
			// logger.info(s_msg);
			msgid = hexString2ByteArray(s_msg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return msgid;
	}

	public static byte[] GetRspid() {
		String s_msg = "";
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
		byte[] msgid = null;
		try {
			synchronized (obj2) {
				msgi = msgi + 1;
			}
			if (msgi >= 9999)
				msgi = 1000;
			s_msg = time.format(nowTime) + msgi;
			msgid = hexString2ByteArray(s_msg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return msgid;
	}

	public static byte[] hexString2ByteArray(String hex) {
		if (hex.length() % 2 != 0)
			return null;
		byte[] buf = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			char c0 = hex.charAt(i);
			char c1 = hex.charAt(i + 1);
			byte b0 = hexChar2byte(c0);
			byte b1 = hexChar2byte(c1);
			if (b0 < 0 || b0 > 15)
				return null;
			if (b1 < 0 || b1 > 15)
				return null;
			buf[i / 2] = (byte) ((b0 << 4) | b1);
		}
		return buf;
	}

	public static byte hexChar2byte(char c) {
		if (c >= '0' && c <= '9')
			return (byte) (c - '0');
		if (c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		if (c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		return -1;
	}

	public static String byteArray2HexString(byte[] buf) {
		char[] hexTable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			byte b = buf[i];
			byte b0 = (byte) (b & 0x0F);
			b = (byte) (b >>> 4);
			byte b1 = (byte) (b & 0x0F);
			sb.append(hexTable[b1]).append(hexTable[b0]);
		}
		return sb.toString();
	}
	

	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// ���λ
		targets[1] = (byte) ((res >> 8) & 0xff);// �ε�λ
		targets[2] = (byte) ((res >> 16) & 0xff);// �θ�λ
		targets[3] = (byte) (res >>> 24);// ���λ,�޷������ơ�
		return targets;
	}

	public static int byte2int(byte[] res) {
		// һ��byte��������24λ���0x??000000��������8λ���0x00??0000

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | ��ʾ��λ��
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}
	
	public static byte[] getMsgId(String client) {		
		try {	
			StringBuffer sb = new StringBuffer();
			sb.append(client).append(parseDateToString(new Date(), "MMddHHmm")).append(SequenceUtil.getSequence());
			return Hex.decodeHex(sb.toString().toCharArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生成msgid失败,"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 日期转字符串
	 * 
	 * @return String
	 * @author kevin
	 */
	public static String parseDateToString(Date thedate, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(thedate.getTime());
	}

	public static byte[] getStandardMsgID() {
		Calendar current = Calendar.getInstance();
		int month = current.get(Calendar.MONTH) + 1;
		int day = current.get(Calendar.DATE);
		int hour = current.get(Calendar.HOUR_OF_DAY);
		int minute = current.get(Calendar.MINUTE);
		int second = current.get(Calendar.SECOND);
		byte[] bs = new byte[8];
		bs[0] |= month << 4;
		bs[0] |= day >> 1;
		bs[1] |= day << 7;
		bs[1] |= hour << 2;
		bs[1] |= (minute << 2) >> 6;
		bs[2] |= minute << 4;
		bs[2] |= second >> 2;
		//bs[3] |= second << 4;
		bs[3] |= (gateway >> 16) & 0x3f;
		bs[4] |= gateway >> 8;
		bs[5] |= gateway;
    	synchronized (obj) {
			bs[6] |= sequence >> 8;
			bs[7] |= sequence;
			sequence++;
		}
		if (sequence == 65535)
			sequence = 1;
		return bs;
	}



}
