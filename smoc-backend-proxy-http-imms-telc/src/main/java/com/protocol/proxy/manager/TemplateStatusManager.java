package com.protocol.proxy.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.proxy.message.StatusMessage;
import com.protocol.proxy.util.DAO;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class TemplateStatusManager extends SuperQueueWorker<StatusMessage> {
	//平台模板状态 0无效、1：拒绝、2：审核通过、3：待审核
	/**
	 * 通道模板状态
	 * 0: 审核失败；
	 * 1：已提交，待审核；
	 * 2：一审通过；
	 * 3~9: N 审通过；
	 * 10：终审通过；必选
	 */

	/**
	 * 通道模板状态和平台模板状态的对应关系Map
	 */
	private static final Map<String, String> CHANNEL_TEMPLATE_STATUS_MAP = new HashMap<String, String>();
	static {
		CHANNEL_TEMPLATE_STATUS_MAP.put("0", String.valueOf(FixedConstant.TemplateStatus.REJECT.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("1", String.valueOf(FixedConstant.TemplateStatus.NO_APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("2", String.valueOf(FixedConstant.TemplateStatus.NO_APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("3", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("4", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("5", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("6", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("7", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("8", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("9", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
		CHANNEL_TEMPLATE_STATUS_MAP.put("10", String.valueOf(FixedConstant.TemplateStatus.APPROVED.ordinal()));
	}

	private static TemplateStatusManager manager = new TemplateStatusManager();

	public static TemplateStatusManager getInstance(){
		return manager;
	}

	private TemplateStatusManager(){
		this.setName("TemplateStatusManager");
		this.start();
	}

	@Override
	protected void doRun() throws Exception {
		StatusMessage message = poll();
		if(message!=null) {
			//获取通道推送的模板审核状态
			String checkState = message.getCheckState();
			String platformTemplateStatus = CHANNEL_TEMPLATE_STATUS_MAP.get(checkState);
			if(StringUtils.isNotEmpty(platformTemplateStatus)){
					DAO.updateAccountChannelTemplateInfo(message, platformTemplateStatus);
					logger.info("运营商模板ID={},运营商状态={},转换平台状态={}",message.getTemplateId(),checkState,platformTemplateStatus);
				}else{
					logger.warn("运营商模板ID={},运营商状态={},转换平台状态为空",message.getTemplateId(),checkState);
			}

		}
	}

	public void process(StatusMessage message) {
		add(message);
	}

}
