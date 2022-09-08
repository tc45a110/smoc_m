/**
 * @desc
 * 
 */
package com.base.common.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.AccountInfoDAO;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AccountInfoManager.AccountInfo;
import com.base.common.worker.SuperMapWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AccountInfoManager extends SuperMapWorker<String, AccountInfo> {

	private static AccountInfoManager manager = new AccountInfoManager();

	private AccountInfoManager() {
		loadData();
		this.start();
	}

	public static AccountInfoManager getInstance() {
		return manager;
	}

	@Override
	public void doRun() throws Exception {
		//发送时涉及财务需要指定单独的加载时间
		Thread.sleep(BusinessDataManager.getInstance().getAccountFinanceLoadIntervalTime());
		loadData();
	}

	/**
	 * 获取账号状态
	 * 
	 * @param accountID
	 * @return
	 */
	public String getAccountStatus(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null && StringUtils.isNoneEmpty(accountInfo.getAccountStatus())) {
			switch (accountInfo.getAccountStatus()) {
			case "0":
				return FixedConstant.AccountStatus.CANCEL.name();
			case "1":
				return FixedConstant.AccountStatus.NORMAL.name();
			case "2":
				return FixedConstant.AccountStatus.EDIT.name();
			case "3":
				return FixedConstant.AccountStatus.SUSPEND.name();
			default:
				break;
			}
		}
		return FixedConstant.AccountStatus.CANCEL.name();
	}
	
	/**
	 * 获取账号名称
	 * @param accountID
	 * @return
	 */
	public String getAccountName(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getAccountName();
		}
		return null;
	}
	
	/**
	 * 获取账号发送的运营商
	 * @param accountID
	 * @return
	 */
	public String getAccountCarrier(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getCarrier();
		}
		return null;
	}
	/**
	 * 	获取账号平台下发扩展码
	 * @param accountID
	 * @return
	 */
	public String getAccountExtendCode(String accountID,String accountSubmitSRCID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			int accountRandomExtendCodeLength = accountInfo.getAccountRandomExtendCodeLength();
			String accountExtendCode = accountInfo.getAccountExtendCode();
			//对于长短信只需要第一条进行平台分配码号的扩展
			if(accountExtendCode.length() < accountRandomExtendCodeLength){		
				String randomExtendCode = RandomStringUtils.random(accountRandomExtendCodeLength - accountExtendCode.length(), false, true);
				accountExtendCode = accountExtendCode + randomExtendCode;		
			}
			return accountSubmitSRCID.replaceFirst(accountInfo.getAccountSRCID(), accountExtendCode);
		}
		return "";
	}

	/**
	 * 获取账号平台扩展码
	 * @param accountID
	 * @return
	 */
	public String getAccountExtendCode(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			int accountRandomExtendCodeLength = accountInfo.getAccountRandomExtendCodeLength();
			String accountExtendCode = accountInfo.getAccountExtendCode();

			if(accountExtendCode.length() < accountRandomExtendCodeLength){
				String randomExtendCode = RandomStringUtils.random(accountRandomExtendCodeLength - accountExtendCode.length(), false, true);
				accountExtendCode = accountExtendCode + randomExtendCode;
			}
			return accountExtendCode;
		}
		return "";
	}
	
	/**
	 * 获取账号的优先级
	 * @param accountID
	 * @return
	 */
	public String getAccountPriority(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getAccountPriority();
		}
		return null;
	}
	
	/**
	 * 获取账号配置-是否进审  0.否  1.是
	 * @param accountID
	 * @return
	 */
	public int getExecuteCheckCode(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return Integer.valueOf(accountInfo.getExecuteCheck());
		}
		return 1;
	}
	
	/**
	 * 获取账号的企业标识
	 * @param accountID
	 * @return
	 */
	public String getEnterpriseFlag(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getEnterpriseFlag();
		}
		return null;
	}
	
	/**
	 * 获取账号的企业ID
	 * @param accountID
	 * @return
	 */
	public String getEnterpriseID(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getEnterpriseID();
		}
		return null;
	}
	
	/**
	 * 获取计费账号
	 * @param accountID
	 * @return
	 */
	public String getFinanceAccountID(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getFinanceAccountID();
		}
		return null;
	}
	
	/**
	 * 获取账号的业务类型
	 * @param accountID
	 * @return
	 */
	public String getBusinessType(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getBusinessType();
		}
		return null;
	}
	
	/**
	 * 获取消息内容的类别
	 * @param accountID
	 * @return
	 */
	public String getInfoType(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getInfoType();
		}
		return null;
	}
	
	/**
	 * 获取账号的付费方式
	 * @param accountID
	 * @return
	 */
	public String getPayType(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			switch (accountInfo.getPayType()) {
			case "1":
				return FixedConstant.AccountPayType.PREPAYMENT.name();
			case "2":
				return FixedConstant.AccountPayType.POSTPAID.name();
			default:
				break;
			}
		}
		return FixedConstant.AccountPayType.POSTPAID.name();
	}
	
	/**
	 * 计费方式
	 * @param accountID
	 * @return
	 */
	public String getChargeType(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			switch (accountInfo.getChargeType()) {
			case "1":
				return FixedConstant.AccountChargeType.SUBMIT.name();
			case "2":
				return FixedConstant.AccountChargeType.REPORT.name();
			default:
				break;
			}
		}
		return FixedConstant.AccountChargeType.SUBMIT.name();
	}
	
	/**
	 * 是否进行模板匹配
	 * @param accountID
	 * @return
	 */
	public String getTemplateMatch(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return getTemplateMatch(accountID);
		}
		return FixedConstant.AccountChargeType.SUBMIT.name();
	}

	
	/**
	 * 获取账号的行业分类
	 * @param accountID
	 * @return
	 */
	public String getIndustryTypes(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getIndustryTypes();
		}
		return "";
	}
	
	/**
	 * 获取账号是否支持携号转网
	 * @param accountID
	 * @return
	 */
	public String getAccountTransferType(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getTransferType();
		}
		return null;
	}
	
	/**
	 * 获取财务账号的余额，若共享则获取共享账号的余额: 可用金额+授信额度
	 * @param accountID
	 * @return
	 */
	public boolean isPositiveAvailableAmount(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getAccountUsableSum() + accountInfo.getAccountCreditSum() > 0;
		}
		return false;
	}

	/**
	 * 获取财务账号的余额，若共享则获取共享账号的余额: 可用金额+授信额度
	 * @param accountID
	 * @return
	 */
	public double getAccountAvailableAmount(String accountID) {
		AccountInfo accountInfo = get(accountID);
		if (accountInfo != null) {
			return accountInfo.getAccountUsableSum() + accountInfo.getAccountCreditSum();
		}
		return 0d;
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		long startTime = System.currentTimeMillis();

		Map<String, Map<String, Object>> accountFinanceResultMap = AccountInfoDAO.loadAccountFinance();
		Map<String, Map<String, Object>> accountBaseInfoResultMap = AccountInfoDAO.loadAccountBaseInfo();

		// 获取账号信息和账号财务数据都获取成功
		if (accountFinanceResultMap != null && accountBaseInfoResultMap != null ) {
			Map<String, AccountInfo> accountInfoMap = new HashMap<String, AccountInfo>();

			for (Map.Entry<String, Map<String, Object>> entry : accountFinanceResultMap.entrySet()) {
				String accountID = entry.getKey();
				AccountInfo accountInfo = new AccountInfo(accountID);
				Map<String, Object> accountFinanceMap = accountFinanceResultMap.get(accountID);

				if (accountFinanceMap != null) {
					// 先判断是否存在共享财务
					String shareAccountID = (String) accountFinanceMap.get("SHARE_ACCOUNT_ID");

					if (shareAccountID != null && shareAccountID.length() > 0) {
						accountInfo.setFinanceAccountID(shareAccountID);
						// 当存在共享财务则获取共享财务的数据
						accountFinanceMap = accountFinanceResultMap.get(shareAccountID);
					} else {
						accountInfo.setFinanceAccountID(accountID);
					}

					Double accountUsableSum = (Double) accountFinanceMap.get("ACCOUNT_USABLE_SUM");
					Double accountCreditSum = (Double) accountFinanceMap.get("ACCOUNT_CREDIT_SUM");
					accountInfo.setAccountUsableSum(accountUsableSum);
					accountInfo.setAccountCreditSum(accountCreditSum);

					accountInfoMap.put(accountID, accountInfo);
				} else {
					CategoryLog.commonLogger.error("{}未获取到财务数据", accountID);
				}

				Map<String, Object> accountBaseInfoMap = accountBaseInfoResultMap.get(accountID);
				// 获取账号信息和配置
				if (accountBaseInfoMap != null) {
					String accountName = (String) accountBaseInfoMap.get("ACCOUNT_NAME");
					String accountStatus = (String) accountBaseInfoMap.get("ACCOUNT_STATUS");
					String accountPriority = (String) accountBaseInfoMap.get("ACCOUNT_PRIORITY");
					String enterpriseFlag = (String) accountBaseInfoMap.get("ENTERPRISE_FLAG");
					String transferType = (String) accountBaseInfoMap.get("TRANSFER_TYPE");
					String businessType = (String) accountBaseInfoMap.get("BUSINESS_TYPE");
					String infoType = (String) accountBaseInfoMap.get("INFO_TYPE");
					String accountExtendCode = (String) accountBaseInfoMap.get("EXTEND_CODE");
					String protocol = (String) accountBaseInfoMap.get("PROTOCOL");
					String accountSrcId = (String) accountBaseInfoMap.get("SRC_ID");

					int accountRandomExtentCodeLength = (Integer) accountBaseInfoMap.get("RANDOM_EXTEND_CODE_LENGTH");
					String executeCheck = (String) accountBaseInfoMap.get("EXECUTE_CHECK");
					String payType = (String) accountBaseInfoMap.get("PAY_TYPE");
					String chargeType = (String) accountBaseInfoMap.get("CHARGE_TYPE");
					String templateMatch = (String) accountBaseInfoMap.get("MATCHING_CHECK");
					String industryTypes = (String) accountBaseInfoMap.get("INDUSTRY_TYPE");
					String carrier = (String) accountBaseInfoMap.get("CARRIER");
					String enterpriseID = (String) accountBaseInfoMap.get("ENTERPRISE_ID");
					
					accountInfo.setAccountName(accountName);
					accountInfo.setAccountStatus(accountStatus);
					accountInfo.setAccountPriority(accountPriority);
					accountInfo.setEnterpriseFlag(enterpriseFlag);
					accountInfo.setTransferType(transferType);
					accountInfo.setBusinessType(businessType);
					accountInfo.setInfoType(infoType);
					accountInfo.setAccountExtendCode(accountExtendCode);
					accountInfo.setProtocol(protocol);
					accountInfo.setAccountSRCID(accountSrcId);
					accountInfo.setAccountRandomExtendCodeLength(accountRandomExtentCodeLength);
					accountInfo.setExecuteCheck(executeCheck);
					accountInfo.setPayType(payType);
					accountInfo.setChargeType(chargeType);
					accountInfo.setTemplateMatch(templateMatch);
					accountInfo.setIndustryTypes(industryTypes);
					accountInfo.setCarrier(carrier);
					accountInfo.setEnterpriseID(enterpriseID);
				} else {
					CategoryLog.commonLogger.error("{}未获取到账号基本信息", accountID);
				}
			}

			superMap = accountInfoMap;
		}

		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}", size(), (endTime - startTime));
	}

	class AccountInfo {

		/**
		 * 业务账号ID
		 */
		private String accountID;

		/**
		 * 业务账号名称
		 */
		private String accountName;

		/**
		 * 业务账号状态
		 */
		private String accountStatus;

		/**
		 * 业务账号优先级
		 */
		private String accountPriority;

		/**
		 * 计费账号ID
		 */
		private String financeAccountID;

		/**
		 * 计费账号的可用余额
		 */
		private double accountUsableSum;

		/**
		 * 计费账号的授信额度，如果计费账号为共享账号，此处为共享账号的授信额度
		 */
		private double accountCreditSum;

		/**
		 * 企业标识
		 */
		private String enterpriseFlag;
		
		/**
		 * 企业ID
		 */
		private String enterpriseID;
		
		/**
		 * 接口协议
		 */
		private String protocol;

		/**
		 * 业务账号携转标识
		 */
		private String transferType;

		/**
		 * 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
		 */
		private String businessType;

		/**
		 * 付费方式
		 */
		private String payType;
		
		/**
		 * 计费方式:
		 */
		private String chargeType;

		/**
		 * 账号接入码
		 */
		private String accountSRCID;

		/**
		 * 账号在平台的扩展码包含随机扩展码加上账号自带扩展码
		 */
		private String accountExtendCode;

		/**
		 * 随机扩展码长度
		 */
		private int accountRandomExtendCodeLength;

		/**
		 * 行业分类：多个行业用&分隔
		 */
		private String industryTypes;

		/**
		 * 信息分类:按照投诉高低分为：行业、会销、拉新、催收
		 */
		private String infoType;

		/**
		 * 是否进入审核
		 */
		private String executeCheck;

		/**
		 * 是否进行模板匹配
		 */
		private String templateMatch;
		
		/**
		 * 发送运营商
		 */
		private String carrier;

		public String getAccountID() {
			return accountID;
		}

		public void setAccountID(String accountID) {
			this.accountID = accountID;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountStatus() {
			return accountStatus;
		}

		public void setAccountStatus(String accountStatus) {
			this.accountStatus = accountStatus;
		}

		public String getAccountPriority() {
			return accountPriority;
		}

		public void setAccountPriority(String accountPriority) {
			this.accountPriority = accountPriority;
		}

		public String getFinanceAccountID() {
			return financeAccountID;
		}

		public void setFinanceAccountID(String financeAccountID) {
			this.financeAccountID = financeAccountID;
		}

		public double getAccountUsableSum() {
			return accountUsableSum;
		}

		public void setAccountUsableSum(double accountUsableSum) {
			this.accountUsableSum = accountUsableSum;
		}

		public double getAccountCreditSum() {
			return accountCreditSum;
		}

		public void setAccountCreditSum(double accountCreditSum) {
			this.accountCreditSum = accountCreditSum;
		}

		public String getEnterpriseFlag() {
			return enterpriseFlag;
		}

		public void setEnterpriseFlag(String enterpriseFlag) {
			this.enterpriseFlag = enterpriseFlag;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public String getTransferType() {
			return transferType;
		}

		public void setTransferType(String transferType) {
			this.transferType = transferType;
		}

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getAccountSRCID() {
			return accountSRCID;
		}

		public void setAccountSRCID(String accountSRCID) {
			this.accountSRCID = accountSRCID;
		}

		public String getAccountExtendCode() {
			return accountExtendCode;
		}

		public void setAccountExtendCode(String accountExtendCode) {
			this.accountExtendCode = accountExtendCode;
		}

		public int getAccountRandomExtendCodeLength() {
			return accountRandomExtendCodeLength;
		}

		public void setAccountRandomExtendCodeLength(int accountRandomExtendCodeLength) {
			this.accountRandomExtendCodeLength = accountRandomExtendCodeLength;
		}

		public String getIndustryTypes() {
			return industryTypes;
		}

		public void setIndustryTypes(String industryTypes) {
			this.industryTypes = industryTypes;
		}

		public String getInfoType() {
			return infoType;
		}

		public void setInfoType(String infoType) {
			this.infoType = infoType;
		}

		public String getExecuteCheck() {
			return executeCheck;
		}

		public void setExecuteCheck(String executeCheck) {
			this.executeCheck = executeCheck;
		}

		public String getTemplateMatch() {
			return templateMatch;
		}

		public void setTemplateMatch(String templateMatch) {
			this.templateMatch = templateMatch;
		}
		
		public String getCarrier() {
			return carrier;
		}

		public void setCarrier(String carrier) {
			this.carrier = carrier;
		}

		public AccountInfo(String accountID) {
			this.accountID = accountID;
		}
		
		public String getChargeType() {
			return chargeType;
		}

		public void setChargeType(String chargeType) {
			this.chargeType = chargeType;
		}

		public String getEnterpriseID() {
			return enterpriseID;
		}

		public void setEnterpriseID(String enterpriseID) {
			this.enterpriseID = enterpriseID;
		}

		@Override
		public String toString() {
			return "AccountInfo [accountID=" + accountID + ", accountName=" + accountName + ", accountStatus="
					+ accountStatus + ", accountPriority=" + accountPriority + ", financeAccountID=" + financeAccountID
					+ ", accountUsableSum=" + accountUsableSum + ", accountCreditSum=" + accountCreditSum
					+ ", enterpriseFlag=" + enterpriseFlag + ", enterpriseID=" + enterpriseID + ", protocol=" + protocol
					+ ", transferType=" + transferType + ", businessType=" + businessType
					+ ", payType=" + payType + ", chargeType=" + chargeType + ", accountSRCID=" + accountSRCID
					+ ", accountExtendCode=" + accountExtendCode + ", accountRandomExtendCodeLength="
					+ accountRandomExtendCodeLength + ", industryTypes=" + industryTypes + ", infoType=" + infoType
					+ ", executeCheck=" + executeCheck + ", templateMatch=" + templateMatch + ", carrier=" + carrier
					+ "]";
		}
	}
}
