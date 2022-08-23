/**
 * @desc
 * 
 */
package com.protocol.access.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.CategoryLog;
import com.protocol.access.client.ClientManager;
import com.protocol.access.vo.Report;

public class DeliverUtil {
	private static final Logger logger = LoggerFactory.getLogger(DeliverUtil.class);
	
	/**
	 * 封装cmpp标准协议回执
	 * @param report
	 * @return
	 * @throws DecoderException 
	 */
	private static com.protocol.access.sgip.pdu.Report packageDeliver(Report report) throws DecoderException{
		com.protocol.access.sgip.pdu.Report delive = new com.protocol.access.sgip.pdu.Report();
		delive.setReport(report);
		delive.setSequenceId(SequenceUtil.getSequence());
		delive.setSubmitSequenceNum(Hex.decodeHex(report.getMessageId()));
		delive.setReportType((byte)0);
		StringBuffer mobile = new StringBuffer().append("86").append(report.getPhoneNumber());
		delive.setUserNumber(mobile.toString());
		String stateCode = report.getStatusCode();
		byte state = (byte)-1;
		if(InsideStatusCodeConstant.StatusCode.DELIVRD.name().equals(stateCode)) {
			state = (byte)0;
		}else {
			try {
				state = Byte.valueOf(stateCode);
			}catch (Exception e) {
				logger.warn("状态码转换错误,messageID={},stateCode={}",report.getMessageId(),stateCode);
				state = (byte)99;
			}
		}
		delive.setState(state == (byte)0 ? (byte)0 : (byte)2);
		delive.setErrorCode(state);
		
		delive.setReserve("");
		//记录状态报告的参数
		CategoryLog.messageLogger.info("SGIP_DELIVER{}{}",FixedConstant.LOG_SEPARATOR,delive.toString());
		return delive;
	}
	
	public static void sendReport(Report report){
		try {
			//设置推送次数
			report.setReportPushTimes(report.getReportPushTimes() + 1);

			com.protocol.access.sgip.pdu.Report delive = packageDeliver(report);
			ClientManager.getInstance().pushRequest(delive, report.getAccountId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
}


