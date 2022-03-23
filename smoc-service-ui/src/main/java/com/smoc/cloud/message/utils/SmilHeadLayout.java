package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "layout")
public class SmilHeadLayout {

    @XmlElement
    private SmilHeadLayoutRootLayout rootLayout;

    public SmilHeadLayout(){}

    public SmilHeadLayout(SmilHeadLayoutRootLayout rootLayout){
        this.rootLayout = rootLayout;
    }
}
