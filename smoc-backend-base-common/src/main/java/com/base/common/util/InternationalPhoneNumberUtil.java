package com.base.common.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.base.common.manager.ResourceManager;
import org.apache.commons.lang3.StringUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

public class InternationalPhoneNumberUtil {
	private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    
	/**
	 * 根据手机号获取国家的中文名称
	 * @param mobile
	 * @return
	 */
	public static String getInternationalAreaName(String mobile){
		if(!mobile.startsWith("0") && !mobile.startsWith("+")) {
			mobile = "+" + mobile;
		}
	    Phonenumber.PhoneNumber swissNumberProto = null;
		String internationalAreaName = null;
		try {
			swissNumberProto = phoneNumberUtil.parse(mobile, "CH");
			internationalAreaName = geocoder.getDescriptionForNumber(swissNumberProto, Locale.CHINESE);
			return getInternationalCodeConversionByCode(internationalAreaName, mobile);
		} catch (NumberParseException e) {
			e.printStackTrace();
		}
	    return null;	
	}
	
	/**
	 * 根据手机号获取国家的手机区号
	 * @param mobile
	 * @return
	 */
	public static Integer getCountryCode(String mobile) {
		if(!mobile.startsWith("0") && !mobile.startsWith("+")) {
			mobile = "+" + mobile;
		}
		PhoneNumber swissNumberProto = null;
		try {
		  phoneNumberUtil.getSupportedRegions();
		  swissNumberProto = phoneNumberUtil.parse(mobile, "CH");
		  return swissNumberProto.getCountryCode();
		} catch (NumberParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String getInternationalCodeConversionByCode(String areaName, String mobile){
		mobile = mobile.replace("+","");
		mobile = mobile.replaceFirst("^(0+)","");
		if(StringUtils.isNotEmpty(areaName)){
			String internationalCodeConversionParams = ResourceManager.getInstance().getValue("international_code_conversion");
			if(StringUtils.isNotEmpty(internationalCodeConversionParams)){
				String[] split = internationalCodeConversionParams.split(";");
				for (String s : split) {
					String [] internationalCodeConversionArr = s.split("-");
					if(areaName.equals(internationalCodeConversionArr[0]) && mobile.startsWith(internationalCodeConversionArr[1])){
						return internationalCodeConversionArr[2];
					}
				}
			}
		}
		// 没匹配上
		return areaName;
	}

	public static void main(String[] args) {
		//System.out.println("18084517786".replaceFirst("^(0+)",""));
		System.out.println(getInternationalAreaName("+0018084517786"));
		//System.out.println(getCountryCode("0018084517786"));
	}
}
