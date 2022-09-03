package com.protocol.proxy.message;

public class StatusMessage {
    private String checkState;
    private String reason;
    private String date;
    private String templateId;
    private String channelID;

    public String getChannelID() {
        return channelID;
    }
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
    public String getCheckState() {
        return checkState;
    }
    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
