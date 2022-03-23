package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "head")
public class SmilHead {

    @XmlElement
    private SmilHeadLayout headLayout = null;

    public SmilHead(){}

    public SmilHead(SmilHeadLayout headLayout){
        this.headLayout = headLayout;
    }
}
