package com.inse.util;

import java.util.regex.Pattern;
public class MyStringUtils {
	static	Pattern pattern = Pattern.compile("\\$\\{\\d{1,2}}");
	
	public static boolean checkTemplate(String template) {		
		String[] lines = template.split("\r\n");
		for(String line: lines){
			if(pattern.matcher(line).find()) {
				return true;
			}
		}
		return false;
	}

}
