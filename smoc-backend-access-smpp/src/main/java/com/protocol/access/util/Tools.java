package com.protocol.access.util;

import com.base.common.util.DateUtil;

public class Tools {
	private static Object obj = new Object();
	private static int sequence = 4097;


	public static byte[] getStandardMsgID(){
		StringBuilder sb = new StringBuilder();
		sb.append(Integer.toHexString(Integer.parseInt(DateUtil.getCurDateTime("ddHHmm"))+65536));
		synchronized (obj) {
			sequence++;
		}
		if (sequence == 65535)
			sequence = 4097;
		sb.append(Integer.toHexString(sequence));
		return sb.toString().getBytes();
	}
	
	public static int byte2int(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
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
	
	public static void main(String[] args) {
		for(int i =0;i<1000;i++){
			System.out.println(new String(getStandardMsgID()));
		}
		
	}
}
