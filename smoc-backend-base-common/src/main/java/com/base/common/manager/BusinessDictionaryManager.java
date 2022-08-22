/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.DictionariesTypeConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.util.InternationalPhoneNumberUtil;

public class BusinessDictionaryManager {
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessDictionaryManager.class);
	
	private static BusinessDictionaryManager manager = new BusinessDictionaryManager();
	
	
	private BusinessDictionaryManager(){
		
	}
	
	public static BusinessDictionaryManager getInstance(){
		return manager;
	}

	/**
	 * 根据省份名称获取省份编码
	 * @param provinceName
	 * @return
	 */
	public String getProvinceCode(String provinceName){
		return DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.PROVINCES, provinceName);
	}
	
	/**
	 * 根据国家名称获取国家编码
	 * @param provinceName
	 * @return
	 */
	public String getInternationalAreaCode(String internationalAreaName){
		return DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.INTERNATIONAL_AREA, internationalAreaName);
	}
	
	/**
	 * 根据手机号获取国家编码
	 * @param internationalAreaName
	 * @return
	 */
	public String getInternationalAreaCodeByPhoneNumber(String phoneNumber){
		String internationalAreaName = InternationalPhoneNumberUtil.getInternationalAreaName(phoneNumber);
		if(StringUtils.isEmpty(internationalAreaName)) {
			//TODO 
			return "";
		}
		String areaCode = getInternationalAreaCode(internationalAreaName);
		if(StringUtils.isEmpty(areaCode)) {
			logger.warn(new StringBuilder().append("获取区域编码失败")
					.append("{}phoneNumber={}")
					.append("{}internationalAreaName={}")
					.toString(),
					FixedConstant.SPLICER,phoneNumber,
					FixedConstant.SPLICER,internationalAreaName);
		}
		return areaCode;
	}
	
	/**
	 * 转换状态码
	 * @param filterResponseCode 过滤服务返回状态码
	 * @return
	 */
	public String getFilterStatusCode(String filterResponseCode){
		return DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.HTTP_FILTER_STATUS_CODE, filterResponseCode);
	}
	
	/**
	 * 根据号码获取国内号段运营商
	 * @param phoneNumber
	 * @return
	 */
	public String getInternalSegmentCarrier(String phoneNumber){
		if (match(DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.CARRIER_SEGMENT,
				FixedConstant.Carrier.CMCC.name()), phoneNumber)) {
			return FixedConstant.Carrier.CMCC.name();
		}
		if (match(DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.CARRIER_SEGMENT,
				FixedConstant.Carrier.UNIC.name()), phoneNumber)) {
			return FixedConstant.Carrier.UNIC.name();
		}
		if (match(DictionaryDataManager.getInstance().getDictionariesCode(DictionariesTypeConstant.CARRIER_SEGMENT,
				FixedConstant.Carrier.TELC.name()), phoneNumber)) {
			return FixedConstant.Carrier.TELC.name();
		}
		return "";
	}
	
	/**
     * 匹配函数
     * @param regex
     * @param tel
     * @return
     */
    private boolean match(String regex, String phoneNumber) {
        return Pattern.matches(regex, phoneNumber);
    }
}


