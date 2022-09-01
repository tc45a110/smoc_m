package com.inse.util;
import com.base.common.dao.LavenderDBSingleton;
import com.inse.message.AccountTemplatelnfo;
import com.inse.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DAO {
    private static final Logger logger = LoggerFactory.getLogger(DAO.class);

    /**获取平台多媒体信息
     * @param channelID
     * @return
     */
    public static List<AccountTemplatelnfo> getaccountTemplateInfo(String channelID){
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt =null;
        ResultSet rs = null;

        List<AccountTemplatelnfo> list = new ArrayList<AccountTemplatelnfo>();
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);
            sql.append("select TEMPLATE_ID,TEMPLATE_CONTENT,MM_ATTCHMENT,BUSINESS_ACCOUNT,TEMPLATE_FLAG from smoc.account_template_info ");
            sql.append("where TEMPLATE_STATUS='2' and TEMPLATE_TYPE='MULTI_SMS'");

            sql.append(" and TEMPLATE_ID not in (select TEMPLATE_ID from smoc.account_channel_template_info where CHANNEL_ID = ?) ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, channelID);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountTemplatelnfo accounttemplateinfo=new AccountTemplatelnfo();

                accounttemplateinfo.setTemplateId(rs.getString("TEMPLATE_ID"));
                accounttemplateinfo.setTemplateTitle(rs.getString("TEMPLATE_CONTENT"));
                accounttemplateinfo.setMmAttchnent(rs.getString("MM_ATTCHMENT"));
                accounttemplateinfo.setBusinessAccount(rs.getString("BUSINESS_ACCOUNT"));
                accounttemplateinfo.setTemplateFlag(rs.getString("TEMPLATE_FLAG"));
                list.add(accounttemplateinfo);
            }
            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);
        }

        return list;
    }


    /**获取账号配置的通道
     * @param businessAccount
     * @return
     */
    public static Set<String> getChannels(String businessAccount) {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        sql.append("select CHANNEL_ID from smoc.account_channel_info where ACCOUNT_ID =? and CHANNEL_STATUS =1");

        Set<String> set = new HashSet<String>();
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, businessAccount);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("CHANNEL_ID"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);
        }
        return set;
    }

    /**
     * 保存运营商模板及平台模板状态信息
     */
    public static void insertAccountChannelTemplateInfo(ResponseMessage response, AccountTemplatelnfo template, String templateStatus, String channelID) {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;

        sql.append("insert into smoc.account_channel_template_info(ID,TEMPLATE_ID,BUSINESS_ACCOUNT,TEMPLATE_STATUS,CHANNEL_ID,CHANNEL_TEMPLATE_ID,CHECK_OPINIONS,TEMPLATE_FLAG,CREATED_TIME)values(?,?,?,?,?,?,?,?,now())");

        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
            pstmt.setString(2,template.getTemplateId());
            pstmt.setString(3,template.getBusinessAccount());
            pstmt.setString(4, templateStatus);
            pstmt.setString(5, channelID);
            pstmt.setString(6,response.getMsgId());
            pstmt.setString(7,response.getMessage());
            pstmt.setString(8,template.getTemplateFlag());

            pstmt.execute();
            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
        }
    }


    /**
     * 更新account_channel_template_info模板状态
     * @param message
     * @param status
     */
    public static void updateAccountChannelTemplateInfo(String channelTemplateStatus,String statusDesc,String channelID,String templateId, int status) {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        sql.append("update smoc.account_channel_template_info set TEMPLATE_STATUS=?,CHANNEL_TEMPLATE_STATUS=?,UPDATED_TIME=now(),CHECK_OPINIONS=? where CHANNEL_ID=? and CHANNEL_TEMPLATE_ID=?");
        logger.info("updateTempldateStatus sql:" + sql.toString());
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1,String.valueOf(status));
            pstmt.setString(2, channelTemplateStatus);
            pstmt.setString(3, statusDesc);
            pstmt.setString(4,channelID);
            pstmt.setString(5,templateId);
            pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }



    }


    //获取审核的通道模板id
    public static List<String> getUnreviewedTempldateID(String channelID) {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        sql.append("select CHANNEL_TEMPLATE_ID from smoc.account_channel_template_info where CHANNEL_ID =? and TEMPLATE_STATUS =3");

        List<String> list = new ArrayList<>();
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, channelID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("CHANNEL_TEMPLATE_ID"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }
        return list;
    }

    public static String getTempldateID(String channelID, String channelTemplateID){
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        sql.append("select TEMPLATE_ID from smoc.account_channel_template_info where CHANNEL_ID =? and CHANNEL_TEMPLATE_ID =?");
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, channelID);
            pstmt.setString(2, channelTemplateID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getString("TEMPLATE_ID");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }
        return "";
    }

}
