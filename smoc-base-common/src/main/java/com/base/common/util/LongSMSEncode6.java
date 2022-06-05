package com.base.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongSMSEncode6 {
	protected static final Logger logger = LoggerFactory.getLogger(LongSMSEncode6.class);
	
	public static final String CHARSET_UCS2 = "UTF-16BE";
	public static final byte[] MSG_HEAD = { 5, 0, 3, 0, 0, 0 };
	public static int LENGTH_MSG_HEAD = MSG_HEAD.length;
	public static int SMSMAXLENGTH = 140;
	public static final int MaxExtBodyLen = SMSMAXLENGTH - LENGTH_MSG_HEAD;

	public static byte[][] enCodeBytes(String longSMS) {
		byte[] dlDataByte = null;
		try {
			dlDataByte = longSMS.getBytes("UTF-16BE");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return getMultiSMSBinaryData(dlDataByte, getId());
	}

	public static byte[] getOneMultiSMSBinaryDatabyte(String content,
			int totalSMSAmount, int number, byte longSMSID) {
		byte[] oneBigSMS = null;
		try {
			oneBigSMS = content.getBytes("UTF-16BE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] result = new byte[oneBigSMS.length + LENGTH_MSG_HEAD];
		fillHead(result, totalSMSAmount, number, longSMSID);
		System.arraycopy(oneBigSMS, 0, result, LENGTH_MSG_HEAD,
				oneBigSMS.length);
		return result;
	}

	private static byte[][] getMultiSMSBinaryData(byte[] oneBigSMS,
			byte longSMSID) {

		logger.debug("oneBigSMS.length  = " + oneBigSMS.length);
		
		int totalSMSAmount = (int) Math
		.ceil((double) oneBigSMS.length / MaxExtBodyLen);

		logger.debug("totalSMSAmount  = " + totalSMSAmount);
		byte[][] results = new byte[totalSMSAmount][];

		for (int j = 0; j < totalSMSAmount; j++) {
			int length = j != totalSMSAmount - 1 ? SMSMAXLENGTH
					: oneBigSMS.length - MaxExtBodyLen * j + LENGTH_MSG_HEAD;

			logger.debug("length of SMS packet [ " + j + " ] is " + length);
			results[j] = new byte[length];
			fillHead(results[j], totalSMSAmount, j + 1, longSMSID);
			System.arraycopy(oneBigSMS, MaxExtBodyLen * j, results[j],
					LENGTH_MSG_HEAD, length - LENGTH_MSG_HEAD);
		}

		return results;
	}

	private static byte longSMSID = 0;

	private static synchronized byte getId() {
		if ((longSMSID = (byte) (longSMSID + 1)) >= Byte.MAX_VALUE) {
			longSMSID = 0;
		}
		return longSMSID;
	}

	private static void fillHead(byte[] result, int totalSMSAmount, int number,
			byte longSMSID) {
		if ((number > totalSMSAmount) || (number <= 0)) {
			logger.error("can't generate UDH correctly.");
		}
		System.arraycopy(MSG_HEAD, 0, result, 0, LENGTH_MSG_HEAD);
		result[(LENGTH_MSG_HEAD - 3)] = longSMSID;
		result[(LENGTH_MSG_HEAD - 2)] = ((byte) totalSMSAmount);
		result[(LENGTH_MSG_HEAD - 1)] = ((byte) number);

	}
	
}
