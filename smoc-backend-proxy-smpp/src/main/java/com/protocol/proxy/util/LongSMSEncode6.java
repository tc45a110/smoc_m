package com.protocol.proxy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class LongSMSEncode6 {
	public static final Logger log = Logger.getLogger(LongSMSEncode6.class);
	
	/*
	 * byte 1 : 05, 表示剩余协议头的长度 
	   byte 2 : 00, 这个值在GSM 03.40规范9.2.3.24.1中规定，表示随后的这批超长短信的标识位长度为1（格式中的XX值）。 
	   byte 3 : 03, 这个值表示剩下短信标识的长度 
       byte 4 : XX，这批短信的唯一标志，事实上，SME(手机或者SP)把消息合并完之后，就重新记录，所以这个标志是否唯一并不是很重要。 
       byte 5 : MM, 这批短信的数量。如果一个超长短信总共5条，这里的值就是5。 
       byte 6 : NN, 这批短信的数量。如果当前短信是这批短信中的第一条的值是1，第二条的值是2。 
	 */
	public static final byte[] MSG_HEAD_6 = { 5, 0, 3, 0, 0, 0 };

	public static byte[][] enCodeBytes(String longSMS,String charset,int maxLength,int headLength) {
		byte[] dlDataByte = null;
		try {
			dlDataByte = longSMS.getBytes(charset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return getMultiSMSBinaryData(dlDataByte, getId(),maxLength,headLength);
	}
	
	public static byte[][] gsmEncodeBytes(String longSMS,int maxLength,int headLength) {
		//每条短信的字符数
		int length = (maxLength * 7 - headLength*8)/7;
		log.debug("gsm7bit编码长短信中每条短信容纳最大字符数为:"+length);
		int totalSMSAmount = (int) Math
		.ceil((double) longSMS.length() / length);
		log.debug("gsm7bit编码长短信拆分条数:"+length);
		String content = null;
		byte[][] results = new byte[totalSMSAmount][];
		List<String> contentList = getStrList(longSMS, length);
		byte longSMSID = getId();
		for(int i = 0;i<totalSMSAmount;i++){
			content = contentList.get(i);
			byte[] data = gsm7BitEncode(content);
			results[i] = new byte[data.length + headLength];
			System.arraycopy(MSG_HEAD_6, 0, results[i], 0, headLength);
			fillHead(results[i], totalSMSAmount, i+1,longSMSID, headLength);
			System.arraycopy(data, 0, results[i], headLength, data.length);
		}
		return results;
	}

	private static byte[][] getMultiSMSBinaryData(byte[] oneBigSMS,
			byte longSMSID,int maxLength,int headLength) {
		log.debug("***************getMultiSMSBinaryData begin*************");

		log.debug("oneBigSMS.length  = " + oneBigSMS.length);
		int totalSMSAmount = (int) Math
		.ceil((double) oneBigSMS.length / (maxLength - headLength ));
		System.out.println(totalSMSAmount);
		log.debug("totalSMSAmount  = " + totalSMSAmount);
		byte[][] results = new byte[totalSMSAmount][];
		for (int j = 0; j < totalSMSAmount; j++) {
			int length = j != totalSMSAmount - 1 ? maxLength
					: oneBigSMS.length - (maxLength - headLength ) * j + headLength;

			log.debug("length of SMS packet [ " + j + " ] is " + length);
			results[j] = new byte[length];
			fillHead(results[j], totalSMSAmount, j + 1, longSMSID,headLength);
			System.arraycopy(oneBigSMS, (maxLength - headLength ) * j, results[j],
					headLength, length - headLength);
		}
		log.debug("*******************getMultiSMSBinaryData finished.(Multiple SMS packet) ****************");
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
			byte longSMSID,int headLength) {
		log.debug("*******************fillUDH begin*******************");
		if ((number > totalSMSAmount) || (number <= 0)) {
			log.error("can't generate UDH correctly.");
		}
		
		System.arraycopy(MSG_HEAD_6, 0, result, 0, headLength);
		result[(headLength - 3)] = longSMSID;
		result[(headLength - 2)] = ((byte) totalSMSAmount);
		result[(headLength - 1)] = ((byte) number);
		
	

		log.debug("*******************fillUDH finished*******************");
	}
	
	
	public static List<String> getStrList(String inputString, int length) {
		int size = inputString.length() / length;
		if (inputString.length() % length != 0) {
			size += 1;
		}
		return getStrList(inputString, length, size);
	}
	
	public static List<String> getStrList(String inputString, int length,
			int size) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			String childStr = substring(inputString, index * length,
					(index + 1) * length);
			list.add(childStr);
		}
		return list;
	}
	
	public static String substring(String str, int f, int t) {
		if (f > str.length())
			return null;
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}
	
	/**
	 * GSM7bit方式编码,只支持ASCII范围的字符串
	 * @param strContent
	 * @return
	 */
	public static byte[] gsm7BitEncode(String strContent) {
		// 结果
		byte[] arrResult = null;
		try {
			// 编码方式
			byte[] arrs = strContent.getBytes("ASCII");

			arrResult = new byte[arrs.length - (arrs.length / 8)];
			int intRight = 0;
			int intLeft = 7;
			int intIndex = 0;
			for (int i = 1; i <= arrs.length; i++, intRight++, intLeft--) {
				if (i % 8 == 0) {
					intRight = -1;
					intLeft = 8;
					continue;
				}
				byte newItem = 0;
				if (i == arrs.length) {
					newItem = (byte) (arrs[i - 1] >> intRight);
				} else {
					newItem = (byte) ((arrs[i - 1] >> intRight) | (arrs[i] << intLeft));
				}

				arrResult[intIndex] = newItem;
				intIndex++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrResult;
	}
	
	/**
	 *  GSM7bit方式编码的字节数组，进行解码成字符串
	 * @param d
	 * @return
	 */
	public static String gsm7BitDecode(byte[] data) {
		byte[] d = new byte[data.length];
		System.arraycopy(data, 0, d, 0, data.length);
		
		byte[] other_mask = { (byte) 0x80, (byte) 0xc0, (byte) 0xe0,
				(byte) 0xf0, (byte) 0xf8, (byte) 0xfc, (byte) 0xfe };

		byte[] my_mask = { 0x7f, 0x3f, 0x1f, 0x0f, 0x07, 0x03, 0x01 };
		byte other = 0;
		byte temp = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < d.length; i++) {
			int index = i % 7;
			temp = d[i];
			d[i] = (byte) (d[i] & my_mask[index]);
			d[i] = (byte) (d[i] << index);

			if (index != 0) {
				d[i] = (byte) (d[i] & other_mask[7 - index]);
				other = (byte) (other >> (8 - index));
				other = (byte) (other & my_mask[7 - index]);
				d[i] = (byte) (d[i] | other);
			}
 
			// 先把下一个数据信息拿走
			other = (byte) (temp & other_mask[index]);
			sb.append((char) d[i]);

			if (index == 6) {
				other = (byte) (other >> 1);
				other = (byte) (other & 0x7f);
				sb.append((char) other);
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
		
		String s = "1111";
//		for(int i=0;i < s.length();i++) {
//			if(String.valueOf(s.charAt(i)).getBytes("UTF-16").length == 4) {
//				System.out.println(String.valueOf(s.charAt(i)));
//			}
//		}
//		System.out.println(s.length());
//		System.out.println(s.getBytes("UTF-16").length);
//		String s1 = "对";
//		System.out.println(s1.getBytes("UTF-16").length);
		
		System.out.println(s.length());
		System.out.println(Arrays.toString(s.getBytes("UTF-16")));
		System.out.println(Arrays.toString(s.getBytes("UTF-16BE")));
		 byte[][] msg = enCodeBytes(s,"UTF-16",140,6);
		 System.out.println(msg.length);
	}

}
