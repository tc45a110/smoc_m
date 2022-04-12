package com.smoc.cloud.model;


public class ParamModel {

    //phone     手机号  字段为空，则跳出所有涉及该字段的过滤
    private String phone;
    //message   消息内容
    private String message;
    //account   业务账号 字段为空，则跳出所有涉及该字段的过滤
    private String account;
    //carrier   运营商  CMCC、UNIC、TELC、INTL 字段为空，则跳出所有涉及该字段的过滤
    private String carrier;
    //channelId 通道id
    private String channelId;
    //infoType  信息分类 INDUSTRY、MARKETING、NEW、COLLECTION 字段为空，则跳出所有涉及该字段的过滤
    private String infoType;
    //province  省份编码 字段为空，则跳出所有涉及该字段的过滤
    private String province;
    //短信签名
    private String sign;

    public String getPhone() {
        if (null != phone) {
            phone = phone.trim();
        }
        return phone;
    }

    public void setPhone(String phone) {
        if ("".equals(phone)) phone = null;
        this.phone = phone;
    }

    public String getMessage() {
        if (null != message) {
            message = message.trim();
        }
        return message;
    }

    public void setMessage(String message) {
        if ("".equals(message)) message = null;
        this.message = message;
    }

    public String getAccount() {
        if (null != account) {
            account = account.trim();
        }
        return account;
    }

    public void setAccount(String account) {
        if ("".equals(account)) account = null;
        this.account = account;
    }

    public String getCarrier() {
        if (null != carrier) {
            carrier = carrier.trim();
        }
        return carrier;
    }

    public void setCarrier(String carrier) {
        if ("".equals(carrier)) carrier = null;
        this.carrier = carrier;
    }

    public String getChannelId() {
        if (null != channelId) {
            channelId = channelId.trim();
        }
        return channelId;
    }

    public void setChannelId(String channelId) {
        if ("".equals(channelId)) channelId = null;
        this.channelId = channelId;
    }

    public String getInfoType() {
        if (null != infoType) {
            infoType = infoType.trim();
        }
        return infoType;
    }

    public void setInfoType(String infoType) {
        if ("".equals(infoType)) infoType = null;
        this.infoType = infoType;
    }

    public String getProvince() {
        if (null != province) {
            province = province.trim();
        }
        return province;
    }

    public void setProvince(String province) {
        if ("".equals(province)) province = null;
        this.province = province;
    }

    public String getSign() {
        if (null != sign) {
            sign = sign.trim();
        }
        return sign;
    }

    public void setSign(String sign) {
        if ("".equals(sign)) sign = null;
        this.sign = sign;
    }
}
