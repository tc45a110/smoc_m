/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.util.Set;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;

public class KeyWordFilterManager {
	
	private static KeyWordFilterManager manager = new KeyWordFilterManager();
	
	private KeyWordFilterManager(){
		
	}
	
	public static KeyWordFilterManager getInstance(){
		return manager;
	}
	
	/**
	 * 获取各个分类的关键词
	 * @param keyWordsBusinessType
	 * @param keyWordsType
	 * @param businessID
	 * @return
	 */
	private Set<String> getKeyWordParamValue(String keyWordsBusinessType,String keyWordsType,String businessID){
		if("SYSTEM".equals(keyWordsBusinessType)){
			businessID = "system";
		}
		return KeyWordParameterManager.getInstance().getKeyWordSet(keyWordsBusinessType, businessID, keyWordsType);
	}
	
	/**
	 * 判断内容是否包含集合中的关键词
	 * @param keyWordSet
	 * @param messageContent
	 * @return
	 */
	private boolean contains(Set<String> keyWordSet,String messageContent) {
		for(String keyWord : keyWordSet) {
			if(messageContent.contains(keyWord)) {
				CategoryLog.commonLogger.warn("{}包含关键字{}",messageContent,keyWord);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据信息类别判断内容是否包含黑词
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isBlackKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.BLACK.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据信息类别判断内容是否包含审核词
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isCheckKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.CHECK.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据信息类别判断内容是否包含白词免黑词限制
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackWordKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_WORD.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据信息类别判断内容是否包含白词免审核词限制
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidCheckWordKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_CHECK_WORD.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据信息类别判断内容是否包含白词免频次限制
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidFrequencyKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_FREQUENCY.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据信息类别判断内容是否包含白词免黑名单限制
	 * @param infoType
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackListKeyWordByInfoType(String infoType,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_LIST.name(), infoType);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	
	//----------------------------------------------------------
	
	/**
	 * 运营商级判断内容是否包含黑词
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isBlackKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.BLACK.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 运营商级判断内容是否包含审核词
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isCheckKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CARRIER.name(),
				FixedConstant.KeyWordTypeItem.CHECK.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 运营商级判断内容是否包含白词免黑词限制
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackWordKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CARRIER.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_WORD.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 运营商级判断内容是否包含白词免审核词限制
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidCheckWordKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CARRIER.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_CHECK_WORD.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 运营商级判断内容是否包含白词免频次限制
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidFrequencyKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CARRIER.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_FREQUENCY.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 运营商级判断内容是否包含白词免黑名单限制
	 * @param carrier
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackListKeyWordByCarrier(String carrier,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CARRIER.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_LIST.name(), carrier);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	
	//----------------------------------------------------------
	
	/**
	 * 根据通道级判断内容是否包含黑词
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isBlackKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CHANNEL.name(),
				FixedConstant.KeyWordTypeItem.BLACK.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含审核词
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isCheckKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CHANNEL.name(),
				FixedConstant.KeyWordTypeItem.CHECK.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据通道级判断内容是否包含白词免黑词限制
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackWordKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CHANNEL.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_WORD.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据通道级判断内容是否包含白词免审核词限制
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidCheckWordKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CHANNEL.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_CHECK_WORD.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据通道级判断内容是否包含白词免频次限制
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidFrequencyKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.CHANNEL.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_FREQUENCY.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据通道级判断内容是否包含白词免黑名单限制
	 * @param channelID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackListKeyWordByChannelID(String channelID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.INFO_TYPE.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_LIST.name(), channelID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	
	//----------------------------------------------------------
	/**
	 * 系统级判断内容是否包含黑词
	 * @param messageContent
	 * @return
	 */
	public boolean isBlackKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.BLACK.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含审核词
	 * @param messageContent
	 * @return
	 */
	public boolean isCheckKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.CHECK.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含白词免黑词限制
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackWordKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_WORD.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含白词免审核词限制
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidCheckWordKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_CHECK_WORD.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含白词免频次限制
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidFrequencyKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_FREQUENCY.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 系统级判断内容是否包含白词免黑名单限制
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackListKeyWordBySystem(String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.SYSTEM.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_LIST.name(), null);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	
	//----------------------------------------------------------
	/**
	 * 系统级判断内容是否包含黑词
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isBlackKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.BLACK.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据账号级判断内容是否包含审核词
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isCheckKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.CHECK.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据账号级判断内容是否包含白词免黑词限制
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackWordKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_WORD.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据账号级判断内容是否包含白词免审核词限制
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidCheckWordKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_CHECK_WORD.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据账号级判断内容是否包含白词免频次限制
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidFrequencyKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_FREQUENCY.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	/**
	 * 根据账号级判断内容是否包含白词免黑名单限制
	 * @param accountID
	 * @param messageContent
	 * @return
	 */
	public boolean isWhiteAvoidBlackListKeyWordByAccount(String accountID,String messageContent) {
		Set<String> keyWordSet = getKeyWordParamValue(FixedConstant.KeyWordBigClassifyItem.BUSINESS_ACCOUNT.name(),
				FixedConstant.KeyWordTypeItem.WHITE_AVOID_BLACK_LIST.name(), accountID);
		if (keyWordSet != null) {
			return contains(keyWordSet, messageContent);
		}
		return false;
	}
	
}


