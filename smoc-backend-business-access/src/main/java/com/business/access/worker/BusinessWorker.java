/**
 * @desc
 * 
 */
package com.business.access.worker;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.constant.UnknownConstant;
import com.base.common.log.AccessBusinessLogManager;
import com.base.common.manager.AccountChannelManager;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.AccountPriceManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.BusinessDictionaryManager;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelPriceManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.manager.ResourceManager;
import com.base.common.manager.SegmentProvinceCityManager;
import com.base.common.util.Commons;
import com.base.common.util.DateUtil;
import com.base.common.util.InternationalPhoneNumberUtil;
import com.base.common.util.MNPUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.util.UUIDUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.access.manager.AccountSignManager;
import com.business.access.manager.ChannelWorkerManager;
import com.business.access.manager.ContentRouteManager;
import com.business.access.manager.EnterpriseFlagManager;
import com.business.access.manager.InsideFilterWorkerManager;

public class BusinessWorker extends SuperCacheWorker {

	private BlockingQueue<BusinessRouteValue> businessRouteValueQueue;

	public BusinessWorker(BlockingQueue<BusinessRouteValue> businessRouteValueQueue) {
		super();
		this.businessRouteValueQueue = businessRouteValueQueue;
	}

	@Override
	public void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = businessRouteValueQueue.take();
		long startTime = System.currentTimeMillis();
		//业务属性数据封装
		boolean result = setBusinessRouteValues(businessRouteValue);
		if(result){

			long endTime = System.currentTimeMillis();
			logger.info(
					new StringBuilder().append("业务数据封装").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}channelID={}")
							.append("{}businessMessageID={}")
							.append("{}messageContent={}")
							.append("{}耗时={}").toString(),
					FixedConstant.SPLICER, businessRouteValue.getAccountID(), 
					FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(), 
					FixedConstant.SPLICER,businessRouteValue.getChannelID(), 
					FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent(), 
					FixedConstant.SPLICER, (endTime - startTime));
			if (FixedConstant.Carrier.INTL.name().equals(businessRouteValue.getBusinessCarrier())
					&& FixedConstant.INTL_SKIP_FILTER_SERVICE_CODE.equals(ResourceManager.getInstance().getValue("intl.skip.filter.service.code"))) {
				// 国际短信跳过过滤服务和黑名单服务
				ChannelWorkerManager.getInstance().process(businessRouteValue);
			} else {
				// 进入过滤流程
				InsideFilterWorkerManager.getInstance().process(businessRouteValue);
			}
		}
	}

	/**
	 * 补全缺少的重要参数
	 * 
	 * @param businessRouteValue
	 */
	private void setUnKnownValues(BusinessRouteValue businessRouteValue) {
		if (StringUtils.isEmpty(businessRouteValue.getChannelID())) {
			businessRouteValue.setChannelID(UnknownConstant.UNKNOWN_CHANNELID);
		}
		if (StringUtils.isEmpty(businessRouteValue.getAreaCode())) {
			businessRouteValue.setAreaCode(UnknownConstant.UNKNOWN_AREACODE);
		}
		if (StringUtils.isEmpty(businessRouteValue.getSegmentCarrier())) {
			businessRouteValue.setSegmentCarrier(UnknownConstant.UNKNOWN_CARRIER);
		}
		if (StringUtils.isEmpty(businessRouteValue.getBusinessCarrier())) {
			businessRouteValue.setBusinessCarrier(UnknownConstant.UNKNOWN_CARRIER);
		}
		if (StringUtils.isEmpty(businessRouteValue.getPriceAreaCode())) {
			businessRouteValue.setPriceAreaCode(UnknownConstant.UNKNOWN_PRICEAREACODE);
		}
	}
	
	/**
	 * 根据追加签名的配置调整短信内容
	 * @param businessRouteValue
	 */
	private void setMessageContentByAppendSignature(BusinessRouteValue businessRouteValue){
		//directAppendSignature和clearedAppendSignature不会同时出现
		//在内容头部直接加签名
		String directAppendSignature = BusinessDataManager.getInstance().getDirectAppendSignature(businessRouteValue.getAccountID());
		if(StringUtils.isNotEmpty(directAppendSignature)){
			businessRouteValue.setMessageContent(new StringBuilder(directAppendSignature).append(businessRouteValue.getMessageContent()).toString());
			return;
		}
		//清除内容中的头部和尾部签名后，再追加签名
		String clearedAppendSignature = BusinessDataManager.getInstance().getClearedAppendSignature(businessRouteValue.getAccountID());
		if(StringUtils.isNotEmpty(clearedAppendSignature)){
			String messageContent = MessageContentUtil.clearContentSignatures(businessRouteValue.getMessageContent(), true);
			businessRouteValue.setMessageContent(new StringBuilder(clearedAppendSignature).append(messageContent).toString());
		}
	}
	
	/**
	 * 业务数据封装
	 * @param businessRouteValue
	 * @return
	 */
	private boolean setBusinessRouteValues(BusinessRouteValue businessRouteValue){
	

		String accountID = businessRouteValue.getAccountID();
		String messageContent = businessRouteValue.getMessageContent();
		String phoneNumber = businessRouteValue.getPhoneNumber();
		// 填充参数
		String enterpriseFlag = AccountInfoManager.getInstance().getEnterpriseFlag(accountID);

		EnterpriseFlagManager.getInstance().add(enterpriseFlag);

		String infoType = AccountInfoManager.getInstance().getInfoType(accountID);
		String accountPriority = AccountInfoManager.getInstance().getAccountPriority(accountID);
		int executeCheckCode = AccountInfoManager.getInstance().getExecuteCheckCode(accountID);
		String financeAccountID = AccountInfoManager.getInstance().getFinanceAccountID(accountID);
		String businessType = AccountInfoManager.getInstance().getBusinessType(accountID);
		String consumeType = BusinessDataManager.getInstance().getAccountConsumeType(accountID);
		String industryTypes = AccountInfoManager.getInstance().getIndustryTypes(accountID);
		String accountExtendCode = AccountInfoManager.getInstance().getAccountExtendCode(accountID,
				businessRouteValue.getAccountSubmitSRCID());
		String accountName = AccountInfoManager.getInstance().getAccountName(accountID);
		
		//根据签名调整内容
		setMessageContentByAppendSignature(businessRouteValue);
		
		String messageSignature = MessageContentUtil.getSignatures(businessRouteValue.getMessageContent());
		// 对accountMessageIDs进行判断，当计费条数大于accountMessageID数量，需要补齐
		if (businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER).length < businessRouteValue
				.getMessageTotal()) {
			StringBuilder sb = new StringBuilder(businessRouteValue.getAccountMessageIDs());
			int messageIDNumber = businessRouteValue.getMessageTotal()
					- businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER).length;
			for (int i = 0; i < messageIDNumber; i++) {
				sb.append(FixedConstant.SPLICER);
				// 以第一个accountMessageID进行补充
				sb.append(businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER)[0]);
			}
			businessRouteValue.setAccountMessageIDs(sb.toString());
		}

		businessRouteValue.setMessageSignature(messageSignature);
		businessRouteValue.setEnterpriseFlag(enterpriseFlag);
		businessRouteValue.setInfoType(infoType);
		businessRouteValue.setAccountPriority(accountPriority);
		businessRouteValue.setExecuteCheckCode(executeCheckCode);
		businessRouteValue.setFinanceAccountID(financeAccountID);
		businessRouteValue.setBusinessType(businessType);
		businessRouteValue.setConsumeType(consumeType);
		businessRouteValue.setIndustryTypes(industryTypes);
		businessRouteValue.setBusinessMessageID(UUIDUtil.get32UUID());
		businessRouteValue.setAccountExtendCode(accountExtendCode);
		businessRouteValue.setAccountName(accountName);

		// 获取账号的号码发送范围：国际、移动、联通、电信
		String accountCarrier = AccountInfoManager.getInstance().getAccountCarrier(accountID);
		if (StringUtils.isEmpty(accountCarrier)) {
			logger.error(
					new StringBuilder().append("发送运营商为空").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}messageContent={}").toString(),
					FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
					messageContent);
			return false;
		}

		// 获取号码的号码运营商、业务运营商、区域编码、区域名称、城市名称
		String areaCode = null;
		String cityName = null;
		String areaName = null;
		String segmentCarrier = null;
		String businessCarrier = null;

		if (FixedConstant.Carrier.INTL.name().equals(accountCarrier)) {
			segmentCarrier = FixedConstant.Carrier.INTL.name();
			businessCarrier = FixedConstant.Carrier.INTL.name();

			areaCode = BusinessDictionaryManager.getInstance()
					.getInternationalAreaCodeByPhoneNumber(businessRouteValue.getPhoneNumber());
			areaName = InternationalPhoneNumberUtil.getInternationalAreaName(businessRouteValue.getPhoneNumber());
			cityName = areaName;
		} else {
			// 获取国内号段运营商
			segmentCarrier = BusinessDictionaryManager.getInstance()
					.getInternalSegmentCarrier(businessRouteValue.getPhoneNumber());
			// 获取国内业务运营商
			businessCarrier = MNPUtil.getInternalBusinessCarrier(accountCarrier, businessRouteValue.getPhoneNumber(),
					segmentCarrier, AccountInfoManager.getInstance().getAccountTransferType(accountID));

			areaCode = SegmentProvinceCityManager.getInstance().getProvinceCode(businessRouteValue.getPhoneNumber());
			areaName = SegmentProvinceCityManager.getInstance().getProvinceName(businessRouteValue.getPhoneNumber());
			cityName = SegmentProvinceCityManager.getInstance().getCityName(businessRouteValue.getPhoneNumber());
		}

		if (StringUtils.isEmpty(businessCarrier)) {
			doLog(businessRouteValue);
			logger.error(
					new StringBuilder().append("获取运营商失败").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}messageContent={}").toString(),
					FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
					messageContent);
			simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOROUTE.name(),"");
			return false;
		}
		businessRouteValue.setSegmentCarrier(segmentCarrier);
		businessRouteValue.setBusinessCarrier(businessCarrier);

		if (StringUtils.isEmpty(areaCode)) {
			doLog(businessRouteValue);
			logger.warn(
					new StringBuilder().append("获取区域失败").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}messageContent={}").toString(),
					FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
					messageContent);
			simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOROUTE.name(),"");
			return false;
		}
		businessRouteValue.setAreaCode(areaCode);
		businessRouteValue.setAreaName(areaName);
		businessRouteValue.setCityName(cityName);

		// 设置单价 和 统计当前消息的花费总金额
		String messagePrice;
		if (FixedConstant.Carrier.INTL.name().equals(accountCarrier)) {
			messagePrice = AccountPriceManager.getInstance().getPrice(accountID,areaCode);	
		}else{
			 messagePrice = AccountPriceManager.getInstance().getPrice(accountID, businessCarrier);
		}
		if (StringUtils.isEmpty(messagePrice)) {
			doLog(businessRouteValue);
			logger.warn(
					new StringBuilder().append("获取账号价格失败").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}messageContent={}").append("{}businessCarrier={}").toString(),
					FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
					messageContent, FixedConstant.SPLICER, businessCarrier);
			simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOPRICE.name(),"");
			return false;
		}
		businessRouteValue.setMessagePrice(messagePrice);
		// 计算当前消息消费总额
		double messageAmount = Double.valueOf(messagePrice) * Integer.valueOf(businessRouteValue.getMessageNumber());
		businessRouteValue.setMessageAmount(String.valueOf(messageAmount));

		// 获取配置通道
		String channelID = AccountChannelManager.getInstance().getChannel(accountID, businessCarrier, areaCode);
		if (StringUtils.isEmpty(channelID)) {
			doLog(businessRouteValue);
			logger.warn("无下发通道或账号非正常状态:{}{}{}{}{}{}", FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber,
					FixedConstant.SPLICER, messageContent);
			simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOROUTE.name(),"");
			return false;
		}

		// 获取内容路由的通道ID		
		String contentRouteChannelID = ContentRouteManager.getInstance().mapping(financeAccountID, businessCarrier,
				messageContent, phoneNumber, areaCode);
		if (StringUtils.isNotEmpty(contentRouteChannelID)) {
			channelID = contentRouteChannelID;
			logger.info(
					new StringBuilder().append("内容路由通道").append("{}accountID={}").append("{}phoneNumber={}")
							.append("{}messageContent={}").append("{}contentRouteChannelID={}").toString(),
					FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
					messageContent, FixedConstant.SPLICER, contentRouteChannelID);
		}
		businessRouteValue.setChannelID(channelID);
		businessRouteValue.setChannelSRCID(ChannelInfoManager.getInstance().getChannelSRCID(channelID));
		
		// 获取通道价格
		if (FixedConstant.PriceStyle.AREA_PRICE.name()
				.equals(ChannelInfoManager.getInstance().getPriceStyle(channelID))) {
			businessRouteValue.setPriceAreaCode(areaCode);
			businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, areaCode));
		} else {
			// 省份编码/国家编码,当通道不区分省份计价时，该值为ALL
			businessRouteValue.setPriceAreaCode(Commons.UNIFIED_PRICING_CODE);
			businessRouteValue.setChannelPrice(
					ChannelPriceManager.getInstance().getPrice(channelID, Commons.UNIFIED_PRICING_CODE));
		}
		
		//通道是否需要报备子端口
		String isRegister = ChannelInfoManager.getInstance().getIsRegister(channelID);
		if(FixedConstant.COMMON_SIMPLE_LOGIC_JUDGE_STRING_FLAG.equals(isRegister)) {
			String signRegisterExtendNumber = AccountSignManager.getInstance().getSignRegisterExtendNumber(accountID,channelID,messageSignature.replace("【", "").replace("】", ""),accountExtendCode);
			if(StringUtils.isNotEmpty(signRegisterExtendNumber)) {
				accountExtendCode = signRegisterExtendNumber;
				businessRouteValue.setAccountExtendCode(accountExtendCode);
			}else {
				//1.签名子端口无记录 2.签名子端口在报备中或者未报备 且账号未配置持续下发  模拟状态报告
				doLog(businessRouteValue);
				logger.warn("获取签名子端口扩展码失败:{}{}{}{}{}{}{}{}", FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber,
						FixedConstant.SPLICER, messageContent, FixedConstant.SPLICER, channelID);
				simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOROUTE.name(),"");
				return false;
			}
		}

		// 如果财务账号为预付费，则需判断可用金额是否为正
		if (FixedConstant.AccountPayType.PREPAYMENT.name()
				.equals(AccountInfoManager.getInstance().getPayType(accountID))) {
			if (!AccountInfoManager.getInstance().isPositiveAvailableAmount(financeAccountID)) {
				doLog(businessRouteValue);
				logger.error(
						new StringBuilder().append("余额不足").append("{}accountID={}").append("{}phoneNumber={}")
								.append("{}messageContent={}").toString(),
						FixedConstant.SPLICER, accountID, FixedConstant.SPLICER, phoneNumber, FixedConstant.SPLICER,
						messageContent);
				simulationStatusReport(businessRouteValue,FixedConstant.StatusReportSource.ACCESS.name(),InsideStatusCodeConstant.StatusCode.NOMONEY.name(),"");
				return false;
			}
		}
		
		doLog(businessRouteValue);
		return true;
	}
	
	/**
	 * 模拟回执
	 * @param businessRouteValue
	 * @param statusCodeSource
	 * @param statusCode
	 * @param subStatusCode
	 */
	private void simulationStatusReport(BusinessRouteValue businessRouteValue,String statusCodeSource,String statusCode,String subStatusCode){
		businessRouteValue.setStatusCodeSource(statusCodeSource);
		businessRouteValue.setStatusCode(statusCode);
		businessRouteValue.setSubStatusCode(subStatusCode);
		MessageSubmitFailManager.getInstance().process(businessRouteValue);
	}

	private void doLog(BusinessRouteValue businessRouteValue) {
		// 补全参数
		setUnKnownValues(businessRouteValue);
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MT.name());
		StringBuilder sb = new StringBuilder()
				.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI))
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getEnterpriseFlag())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getProtocol())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountSubmitTime())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getBusinessMessageID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getPhoneNumber())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getSegmentCarrier())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getBusinessCarrier())
				.append(FixedConstant.LOG_SEPARATOR).append(StringUtils.defaultString(businessRouteValue.getAreaCode()))// 10-区域编码
				.append(FixedConstant.LOG_SEPARATOR).append(StringUtils.defaultString(businessRouteValue.getAreaName()))
				.append(FixedConstant.LOG_SEPARATOR).append(StringUtils.defaultString(businessRouteValue.getCityName()))
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountSubmitSRCID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountExtendCode())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountBusinessCode())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getMessageFormat())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getMessageContent()))
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getMessageSignature())// 18-签名
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getMessageNumber())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getInfoType())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getIndustryTypes())// 21-行业分类
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountPriority())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountMessageIDs())

				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getChannelID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getChannelSRCID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getFinanceAccountID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(StringUtils.defaultString(businessRouteValue.getMessagePrice(), "0"))
				.append(FixedConstant.LOG_SEPARATOR)
				.append(StringUtils.defaultString(businessRouteValue.getMessageAmount(), "0"))// 28-计费总额
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getBusinessType())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getAccountTemplateID())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getConsumeType())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getOptionParam())
				.append(FixedConstant.LOG_SEPARATOR).append(businessRouteValue.getPriceAreaCode())
				.append(System.getProperty("line.separator"));
		AccessBusinessLogManager.getInstance().process(sb.toString(), businessRouteValue.getEnterpriseFlag(),
				businessRouteValue.getRouteLabel());
	}
}
