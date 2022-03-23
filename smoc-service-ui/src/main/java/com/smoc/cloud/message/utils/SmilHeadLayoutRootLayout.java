package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "root-layout")
public class SmilHeadLayoutRootLayout {

    private String height;
    @XmlAttribute
    public void setHeight(String height) {
        this.height = height;
    }
    public String getHeight() {
        return this.height;
    }

    private String width;
    @XmlAttribute
    public void setWidth(String width) {
        this.width = width;
    }
    public String getWidth() {
        return this.width;
    }

    @XmlElement
    private List<SmilHeadLayoutRootLayoutHeadRegion> regions;

    public SmilHeadLayoutRootLayout(){}

    public SmilHeadLayoutRootLayout(List<SmilHeadLayoutRootLayoutHeadRegion> regions){
        this.regions = regions;
    }

}
