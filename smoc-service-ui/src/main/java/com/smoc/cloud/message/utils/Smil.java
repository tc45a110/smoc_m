package com.smoc.cloud.message.utils;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "smil")
public class Smil implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xmlns;
    @XmlAttribute
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
    public String getXmlns() {
        return this.xmlns;
    }

    @XmlElement
    private SmilHead head = null;

    @XmlElement
    private SmilBody body = null;

    public SmilBody getBody(){
        return this.body;
    }

    public Smil(){}
    public Smil(SmilHead head, SmilBody body){
        this.head = head;
        this.body = body;
    }
}
