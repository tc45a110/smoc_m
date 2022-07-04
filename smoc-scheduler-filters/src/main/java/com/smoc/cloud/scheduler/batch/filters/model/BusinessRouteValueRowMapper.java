package com.smoc.cloud.scheduler.batch.filters.model;

import com.smoc.cloud.scheduler.tools.utils.InsideStatusCodeConstant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusinessRouteValueRowMapper implements RowMapper<BusinessRouteValue> {
    @Override
    public BusinessRouteValue mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusinessRouteValue qo = new BusinessRouteValue();
        qo.setId(rs.getLong("ID"));
        qo.setAccountId(rs.getString("ACCOUNT_ID"));
        qo.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        qo.setAccountSubmitTime(rs.getString("SUBMIT_TIME"));
        qo.setMessageContent(rs.getString("MESSAGE_CONTENT"));
        qo.setMessageFormat(rs.getString("MESSAGE_FORMAT"));
        qo.setAccountTemplateId(rs.getString("TEMPLATE_ID"));
        qo.setProtocol(rs.getString("PROTOCOL"));
        qo.setAccountSubmitSrcId(rs.getString("ACCOUNT_SRC_ID"));
        qo.setAccountBusinessCode(rs.getString("ACCOUNT_BUSINESS_CODE"));
        qo.setTaskPhoneNumberNumber(rs.getLong("PHONE_NUMBER_NUMBER"));
        qo.setTaskMessageNumber(rs.getLong("MESSAGE_CONTENT_NUMBER"));
        qo.setAccountReportFlag(rs.getInt("REPORT_FLAG"));
        qo.setOptionParam(rs.getString("OPTION_PARAM"));
        qo.setAccountSubmitTime(rs.getString("CREATED_TIME"));
        /**
         * 对下面数据进行加工处理
         */
        qo.setMessageTotal(splitSMSContentNumber(qo.getMessageContent()));
        qo.setAccountMessageIds(handlerMessageIds(rs.getString("MESSAGE_ID"), qo.getMessageTotal()));
        qo.setMessageSignature(analyzeSign(qo.getMessageContent()));
        return qo;
    }


    /**
     * 解析签名
     *
     * @param messageContent
     */
    public String analyzeSign(String messageContent) {
        String signature = null;
        Pattern pattern = Pattern.compile("【.*】");
        Matcher matcher = pattern.matcher(messageContent);
        if (matcher.find()) {
            signature = matcher.group(0);
        }
        return signature;
    }

    /**
     * 加工处理 AccountMessageIds
     *
     * @param accountMessageIds
     * @param messageTotal
     * @return
     */
    public String handlerMessageIds(String accountMessageIds, Integer messageTotal) {
        String[] messageIds = accountMessageIds.split("&");
        Integer messageIdsLength = messageIds.length;

        if (messageIdsLength == messageTotal) {
            return accountMessageIds;
        }

        //补全messageId
        if (messageIdsLength < messageTotal) {
            StringBuffer idsBuffer = new StringBuffer(accountMessageIds);
            for (int i = 0; i < (messageTotal - messageIdsLength); i++) {
                idsBuffer.append("&");
                idsBuffer.append(messageIds[0]);
            }
            return idsBuffer.toString();
        }

        return accountMessageIds;

    }

    /**
     * 短信拆分条数
     *
     * @param content
     * @return
     */
    public int splitSMSContentNumber(String content) {
        return (int) (content.length() <= 70 ? 1 : Math.ceil((double) content.length() / 67));
    }


}
