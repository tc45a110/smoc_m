package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "img")
public class SmilBodyParImg {

    private String region;
    @XmlAttribute
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegion() {
        return this.region;
    }

    private String src;
    @XmlAttribute
    public void setSrc(String src) {
        this.src = src;
    }
    public String getSrc() {
        return this.src;
    }
}
