package com.base.common.util;

public class SignatureUtils {
	
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
	
	public static String getSignatureWithColon(String content) {
		if (!content.startsWith("【")) {
			return "";
		}
		
		if (content.indexOf("】:") > 0) {
			return content
					.substring(content.indexOf("【"), content.indexOf("】:") + 2);
		}
		if (content.indexOf("】：") > 0) {
			return content
					.substring(content.indexOf("【"), content.indexOf("】：") + 2);
		}
		return "";
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
}
