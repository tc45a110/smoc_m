/**
 * @desc
 * @author ma
 * @date 2017��9��5��
 * 
 */
package com.protocol.access.manager;

import java.util.regex.Pattern;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.util.TypeConvert;
import com.protocol.access.vo.AuthClient;
import com.protocol.access.vo.MessageInfo;
import com.protocol.access.cmpp.pdu.Submit;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.util.DateUtil;

public class AuthSubmitMessageManager {
	private static final Logger logger = LoggerFactory.getLogger(AuthSubmitMessageManager.class);

	public static Pattern pattern = Pattern.compile("^-?[0-9]+");

	private static AuthSubmitMessageManager manager = new AuthSubmitMessageManager();

	public static AuthSubmitMessageManager getInstance() {
		return manager;
	}

	private AuthSubmitMessageManager() {
	}

	/**
	 * 校验提交信息的参数
	 * @param session
	 * @param submit
	 * @return
	 */
	public int authSubmitMessage(IoSession session, final Submit submit) {
		final String client = SessionManager.getInstance().getClient(session);
		if (client == null) {
			logger.error("无效session={}", session);
			return 14;
		}
		AuthClient authClientVO = AuthCheckerManager.getInstance().getAuthClient(client);

		if (authClientVO == null) {
			logger.error("无效client={}", client);
			return 110;
		}
		
		if (CacheBaseService.isOverAccountSpeed(client, 1, authClientVO.getMaxSendSecond())) {
			logger.error("client={}超流控", client);
			return 8;
		}

		int status = checkBusinessParameters(submit, client);
		if (status > 0) {
			return status;
		}

		MessageInfo messageInfo = convertMessageInfo(submit, authClientVO);
		long start = System.currentTimeMillis();
		route(messageInfo);
		logger.debug("client={},msgid={},耗时{}毫秒", client,
				String.valueOf(TypeConvert.byte2long(submit.getMsgId())), (System.currentTimeMillis() - start));

		return 0;
	}
	
	/**
	 * 长短信路由到长短信匹配线程中处理
	 * @param messageInfo
	 */
	private void route(MessageInfo messageInfo) {
		if(messageInfo.getTotal() == 1){
			SubmitWorkerManager.getInstance().process(messageInfo);
		}else if(messageInfo.getTotal() > 1){
			LongSubmitManagerFactory.getInstance().persistence(messageInfo);
		}else{
			logger.error(
					new StringBuilder()
					.append("消息格式有错:")
					.append("client={}")
					.append("{}messageInfo={}").toString()
					,
					messageInfo.getAccountId(),
					FixedConstant.LOG_SEPARATOR,messageInfo.toString());
		}
	}

	/**
	 * 将标准协议Submit转换成业务对象SubmitVO
	 * @param submit
	 * @param authClientVO
	 * @return
	 */
	private MessageInfo convertMessageInfo(Submit submit, AuthClient authClientVO) {
		MessageInfo vo = new MessageInfo();
		vo.setAccountId(authClientVO.getAccountID());
		vo.setPhoneNumber(submit.getDestTermId()[0]);
		vo.setSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		vo.setMessageContent(submit.getSm().getMessage());
		
		vo.setMessageFormat(String.valueOf((int)submit.getMsgFormat()));
		vo.setMessageId(String.valueOf(TypeConvert.byte2long(submit.getMsgId())));
		vo.setProtocol(DynamicConstant.PLATFORM_IDENTIFIER);
		vo.setAccountSrcId(submit.getSrcId());
		vo.setAccountBusinessCode(submit.getServiceId());
		vo.setPhoneNumberNumber(1);
		vo.setMessageContentNumber(submit.getSm().getTotal());
		vo.setReportFlag(submit.getNeedReport());
		vo.setTemplateId(submit.getLinkId());
		
		vo.setTotal(submit.getSm().getTotal());
		vo.setNumber(submit.getSm().getNumber());
		vo.setLongsmid(submit.getSm().getLongsmsid());
		return vo;
	}

	/**
	 * 校验业务参数
	 * @param submit
	 * @param client
	 * @return
	 */
	private int checkBusinessParameters(Submit submit, String client) {

		if ((submit.getSrcId() == null || !submit.getSrcId().startsWith(AuthCheckerManager.getInstance().getAuthClient(client).getSrcId())) 
				&& !pattern.matcher(submit.getSrcId()).matches()) {
			logger.error(
					new StringBuilder()
					.append("src_id错误:")
					.append("client={}")
					.append("{}srcID={}").toString()
					,
					client,
					FixedConstant.LOG_SEPARATOR,submit.getSrcId());
			return 10;
		}

		return 0;
	}

}
