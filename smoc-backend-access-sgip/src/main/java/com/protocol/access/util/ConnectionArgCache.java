/**
 * @desc
 * 连接参数缓存
 */
package com.protocol.access.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 维护连接参数
 * 			host = argMap.get("host");
			port = Integer.parseInt(argMap.get("port"));
			loginName = argMap.get("loginName");
			loginPassword = argMap.get("loginPassword");
			interval = Integer.parseInt(argMap.get("interval"));
 */
public class ConnectionArgCache {
	private static Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
	
	static public Map<String, String> getArgMap(String client){
		return paramMap.get(client);
	}
	
	static public void putArgMap(String client,Map<String, String> map){
		paramMap.put(client, map);
	}
}


