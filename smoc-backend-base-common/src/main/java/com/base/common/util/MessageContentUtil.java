package com.base.common.util;

import java.util.regex.Pattern;

public class MessageContentUtil {

	
	/**
	 * 获取头部签名
	 * @param content
	 * @return
	 */
	public static String getSignature(String content) {
		if (!content.startsWith("【")) {
			return "";
		}
		if (content.indexOf("】") < 0) {
			return "";
		}
		return content
				.substring(content.indexOf("【"), content.indexOf("】") + 1);
	}
	
	/**
	 * 优先获取头部签名，如果头部无签名，则获取尾部签名,均无签名返回-
	 * @param content
	 * @return
	 */
	public static String getSignatures(String content) {
		String sign = getSignature(content);
		if(sign.length() > 0){
			return sign;
		}
		sign = getEndSignature(content);
		if(sign.length() > 0){
			return sign;
		}
		return "【-】";
	}
	
	/**
	 * 获取尾部签名
	 * @param content
	 * @return
	 */
	public static String getEndSignature(String content) {
		if (!content.endsWith("】")) {
			return "";
		}
		if (content.lastIndexOf("【") < 0) {
			return "";
		}
		return content
				.substring(content.lastIndexOf("【"), content.lastIndexOf("】") + 1);
	}
	
	/**
	 * 短信拆分条数
	 * 
	 * @param content
	 * @return
	 */
	public static int splitSMSContentNumber(String content) {
		return (int) (content.length() <= 70 ? 1 : Math.ceil((double) content
				.length() / 67));
	}
	
	/**
	 * 国际短信拆分条数
	 * @param content
	 * @return
	 */
	public static int splitInternationalSMSContentNumber(String content) {	
		boolean flag = isPureASCII(content);
		//如果是纯英文则按照
		if(flag){
			return (int) (content.length() <= 140 ? 1 : Math.ceil((double) content
					.length() / 134));
		}else{		
			return (int) (content.length() <= 70 ? 1 : Math.ceil((double) content
					.length() / 67));
		}

	}

	/**
	 * 填充模板
	 * 
	 * @param data
	 * @param template
	 * @return
	 */
	public static String fillTemplate(String data, String splitChar,
			String template) {
		int index = 1;
		for (String str : data.split(splitChar)) {
			template = template.replace(new StringBuffer().append("${")
					.append(String.valueOf(index)).append("}").toString(),
					str);
			index++;
		}
		return template;
	}

	public static String fillTemplate(String[] datas, String template) {
		int index = 1;
		for (String str : datas) {
			template = template.replace(new StringBuffer().append("${")
					.append(String.valueOf(index)).append("}").toString(),
					str);
			index++;
		}
		return template;
	}

	static Pattern pattern = Pattern.compile("\\$\\{\\d{1,2}}");

	public static boolean checkTemplate(String template) {
		
		//String[] lines = template.split(System.lineSeparator());
		String[] lines = template.split("\r\n");
		for(String line: lines){
			if(pattern.matcher(line).find()) {
				return true;
			}
		}
		return false;
	}
	
	public static String handlingLineBreakCommas(String text){
		if(text == null){
			return "-";
		}
		return text.replaceAll(",", "，")
				.replaceAll("[\\t\\n\\r]","");
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
	
	private static String CHARSET_ASCII = "US-ASCII";
	/**
	 * 判断字符串是否只有ASCII的字符
	 * @param content
	 * @return
	 */
	public static boolean isPureASCII (String content){
		try {
			if(content.equals(new String(content.getBytes(CHARSET_ASCII),CHARSET_ASCII)))
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
