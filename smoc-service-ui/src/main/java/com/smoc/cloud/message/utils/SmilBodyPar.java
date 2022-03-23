package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "par")
public class SmilBodyPar {

    private String dur;
    @XmlAttribute
    public void setDur(String dur) {
        this.dur = dur;
    }
    public String getDur() {
        return this.dur;
    }

    @XmlElement
    private SmilBodyParImg img;
    public SmilBodyParImg getSmilBodyParImg() {
        return this.img;
    }

    @XmlElement
    private SmilBodyParTxt txt;
    public SmilBodyParTxt getSmilBodyParTxt() {
        return this.txt;
    }

    public SmilBodyPar(){}

    public SmilBodyPar(SmilBodyParImg img, SmilBodyParTxt txt){
        this.img = img;
        this.txt = txt;
    }

    public SmilBodyPar(SmilBodyParImg img){
        this.img = img;
    }

    public SmilBodyPar(SmilBodyParTxt txt){
        this.txt = txt;
    }
}
