package com.smoc.cloud.admin.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

public class DES {

	// 客户端秘钥加密解密key
	private static final String BANK_CRYPT_KEY = "^v@p#m$^";

	// 解密数据
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	public static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
	}


	/**
	 * 银行秘钥加密
	 * 
	 * @param value
	 * @return
	 */
	public static String bank_encrypt(String value) {
		String result = "";
		try {
			value = java.net.URLEncoder.encode(value, "utf-8");
			result = toHexString(encrypt(value, BANK_CRYPT_KEY)).toUpperCase();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		return result;
	}

	/**
	 * 银行秘钥解密
	 * 
	 * @param message
	 * @return
	 */
	public static String bank_decrypt(String message) {

		String resultKey = "";

		try {
			resultKey = decrypt(message, BANK_CRYPT_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultKey;
	}

	public static byte[] convertHexString(String ss) {
        byte[] digest = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	public static String toHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(bank_encrypt("Wjho2020.."));
		//System.out.println(bank_decrypt("D179F8A8A361CCA87656CF42F0F67633BE82EA6D49B41450BFB4B898E8A56903"));

	}
}
