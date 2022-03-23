package com.smoc.cloud.message.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "body")
public class SmilBody {

    @XmlElement
    private List<SmilBodyPar> par;

    public List<SmilBodyPar> getParList(){
        return this.par;
    }

    public SmilBody(){}

    public SmilBody(List<SmilBodyPar> par){
        this.par = par;
    }
}
