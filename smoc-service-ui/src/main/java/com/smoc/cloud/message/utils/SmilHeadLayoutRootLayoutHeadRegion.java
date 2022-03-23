package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "region")
public class SmilHeadLayoutRootLayoutHeadRegion {

    private String id;
    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }

    private String top;
    @XmlAttribute
    public void setTop(String top) {
        this.top = top;
    }
    public String getTop() {
        return this.top;
    }

    private String left;
    @XmlAttribute
    public void setLeft(String left) {
        this.left = left;
    }
    public String getLeft() {
        return this.left;
    }

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

    private String fit;
    @XmlAttribute
    public void setFit(String fit) {
        this.fit = fit;
    }
    public String getFit() {
        return this.fit;
    }
}
