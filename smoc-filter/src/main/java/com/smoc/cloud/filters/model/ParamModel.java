package com.smoc.cloud.filters.model;


public class ParamModel {

    //phone     手机号
    private String phone;
    //message   消息内容
    private String message;
    //account   业务账号
    private String account;
    //carrier   运营商  CMCC、UNIC、TELC、INTL
    private String carrier;
    //channelId 通道id
    private String channelId;
    //infoType  信息分类 INDUSTRY、MARKETING、NEW、COLLECTION
    private String infoType;
    //province  省份编码
    private String province;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
