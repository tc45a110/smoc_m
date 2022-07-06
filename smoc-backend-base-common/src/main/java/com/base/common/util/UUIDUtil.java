/**
 * @desc
 * 
 */
package com.base.common.util;

import java.util.UUID;

public class UUIDUtil {

	/**
	 * 获得4个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String get4UUID() {

		UUID id = UUID.randomUUID();

		String[] idd = id.toString().split("-");

		return idd[1];

	}

	/**
	 * 获得8个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String get8UUID() {

		UUID id = UUID.randomUUID();

		String[] idd = id.toString().split("-");

		return idd[0];

	}

	/**
	 * 获得24个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String get24UUID() {

		UUID id = UUID.randomUUID();

		String[] idd = id.toString().split("-");

		return idd[0] + idd[1] + idd[4];

	}

	/**
	 * 获得32个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String get32UUID() {
		UUID id = UUID.randomUUID();
		return id.toString().replace("-", "");
	}
	
	/**
	 * 获得36个长度的十六进制的UUID
	 */
	public static String get36UUID(){
		return UUID.randomUUID().toString();
	}

}
