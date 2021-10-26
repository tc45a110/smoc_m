package com.smoc.cloud.common.utils;

import java.security.MessageDigest;

/**
 * MD5加密，作为密码的二次加密
 * 
 * @author wujihuio
 *
 */
public class MD5 {

	public static String md5(String source) {

		StringBuffer sb = new StringBuffer(32);

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(source.getBytes("utf-8"));

			for (int i = 0; i < array.length; i++) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
			}
		} catch (Exception e) {
			System.out.println("java Md5 Exception:MD5加密错误");
			return null;
		}

		return sb.toString();
	}

	/**
	 * 对指定的byte数组数据生成MD5码
	 *
	 * @param btInput
	 * @return
	 */
//	public final static String mD5(byte[] btInput) {
//		try {
//			// 获得MD5摘要算法的 MessageDigest 对象
//			MessageDigest mdInst = MessageDigest.getInstance("MD5");
//			// 使用指定的字节更新摘要
//			mdInst.update(btInput);
//			// 获得密文
//			byte[] md = mdInst.digest();
//
//			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char[] str = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//
//			return new String(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

}
