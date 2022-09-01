package com.inse.manager;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.worker.SuperMapWorker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class AccountChannelTemplatelnfoManager extends SuperMapWorker<String, AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo> {

    private static AccountChannelTemplatelnfoManager manager = new AccountChannelTemplatelnfoManager();

    private AccountChannelTemplatelnfoManager() {
        loadData();
        this.setName("AccountChanelTemplateInfoManager");
        this.start();
    }

    public static AccountChannelTemplatelnfoManager getInstance() {
        return manager;
    }

    @Override
    protected void doRun() throws Exception {
        loadData();
        Thread.sleep(INTERVAL);
    }

    /**
     * 获取通道模板id
     *
     * @param templateID
     * @return
     */
    public String getChannelTemplateID(String templateID) {
        AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo templateInfo = get(templateID);
        if (templateInfo != null) {
            return templateInfo.getChannelTemplateID();
        }
        return null;
    }

    /**
     * 获取账号
     *
     * @param
     * @return
     */
    public String getBusinessAccount(String templateID) {
        AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo templateInfo = get(templateID);
        if (templateInfo != null) {
            return templateInfo.getBusinessAccount();
        }
        return null;
    }

    /**
     * 获取账号的扩展码
     *
     * @param
     * @return
     */
    public String getAccountExtendCode(String templateID) {
        AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo templateInfo = get(templateID);
        if (templateInfo != null) {
            return templateInfo.getAccountExtendCode();
        }
        return null;
    }

    /**
     * 获取模板标识
     *
     * @param templateID
     * @return
     */
    public String getTemplateFlag(String templateID) {
        AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo templateInfo = get(templateID);
        if (templateInfo != null) {
            return templateInfo.getTemplateFlag();
        }
        return null;
    }

    /**
     * 获取通道模板变量格式
     *
     * @param templateID
     * @return
     */
    public String getChannelTemplateVariableFormat(String templateID) {
        AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo templateInfo = get(templateID);
        if (templateInfo != null) {
            return templateInfo.getChannelTemplateVariableFormat();
        }
        return null;
    }

    public void loadData() {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        sql.append(
                "select BUSINESS_ACCOUNT, TEMPLATE_ID,CHANNEL_TEMPLATE_ID,CHANNEL_TEMPLATE_VARIABLE_FORMAT,TEMPLATE_FLAG,ACCOUNT_EXTEND_CODE ");
        sql.append(" from smoc.account_channel_template_info where TEMPLATE_STATUS ='2'");

        Map<String, AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo > templateMap = new HashMap<String, AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo>();
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo  accountchanneltemplateinfo = new AccountChannelTemplatelnfoManager.AccountChannelTemplateInfo();
                String templateID = rs.getString("TEMPLATE_ID");
                accountchanneltemplateinfo.setBusinessAccount(rs.getString(("BUSINESS_ACCOUNT")));
                accountchanneltemplateinfo.setChannelTemplateID(rs.getString(("CHANNEL_TEMPLATE_ID")));
                accountchanneltemplateinfo
                        .setChannelTemplateVariableFormat(rs.getString(("CHANNEL_TEMPLATE_VARIABLE_FORMAT")));
                accountchanneltemplateinfo.setTemplateFlag(rs.getString(("TEMPLATE_FLAG")));
                accountchanneltemplateinfo.setAccountExtendCode(rs.getString(("ACCOUNT_EXTEND_CODE")));
                templateMap.put(templateID, accountchanneltemplateinfo);

            }
            superMap = templateMap;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }

    }

    class AccountChannelTemplateInfo {
        /**
         * 通道账号
         */
        private String businessAccount;

        /**
         * 账号的扩展码
         */
        private String accountExtendCode;

        /**
         * 通道模板id
         *
         */
        private String channelTemplateID;

        /**
         * 通道模板状态
         *
         */
        private String channelTemplateStatus;

        /**
         * 通道模板变量格式
         *
         */
        private String channelTemplateVariableFormat;

        /**
         * 变量标识
         *
         */
        private String templateFlag;

        public String getChannelTemplateID() {
            return channelTemplateID;
        }

        public void setChannelTemplateID(String channelTemplateID) {
            this.channelTemplateID = channelTemplateID;
        }

        public String getChannelTemplateStatus() {
            return channelTemplateStatus;
        }

        public void setChannelTemplateStatus(String channelTemplateStatus) {
            this.channelTemplateStatus = channelTemplateStatus;
        }

        public String getChannelTemplateVariableFormat() {
            return channelTemplateVariableFormat;
        }

        public void setChannelTemplateVariableFormat(String channelTemplateVariableFormat) {
            this.channelTemplateVariableFormat = channelTemplateVariableFormat;
        }

        public String getTemplateFlag() {
            return templateFlag;
        }

        public void setTemplateFlag(String templateFlag) {
            this.templateFlag = templateFlag;
        }

        public String getBusinessAccount() {
            return businessAccount;
        }

        public void setBusinessAccount(String businessAccount) {
            this.businessAccount = businessAccount;
        }

        public String getAccountExtendCode() {
            return accountExtendCode;
        }

        public void setAccountExtendCode(String accountExtendCode) {
            this.accountExtendCode = accountExtendCode;
        }

    }
}
