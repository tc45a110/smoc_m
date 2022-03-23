package com.smoc.cloud.message.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {

    public static Object unmarshaller(Class clazz, String xmlContent) throws Exception {
        // 通过映射的类创建XMLComtext上下文对象，其中参数为映射的类。
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        // 通过JAXBContext上下文对象创建createUnmarshaller()方法，创建XML转换成JAVA对象的格式。
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        // 最后，将XML转换成对映的类，转换后需要强制性转换成映射的类
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
        return unmarshaller.unmarshal(is);
    }


    public static String convertToXml(Object obj) throws Exception {
        // 创建输出流
        StringWriter sw = new StringWriter();
        // 利用jdk中自带的转换类实现
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        // 格式化xml输出的格式
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        // 将对象转换成输出流形式的xml
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public static void main(String[] args){
        System.out.println(123);

        SmilHeadLayoutRootLayoutHeadRegion r1 = new SmilHeadLayoutRootLayoutHeadRegion();
        r1.setId("Image");
        r1.setTop("0");
        r1.setLeft("0");
        r1.setHeight("50");
        r1.setWidth("100");
        r1.setFit("hidden");
        SmilHeadLayoutRootLayoutHeadRegion r2 = new SmilHeadLayoutRootLayoutHeadRegion();
        r2.setId("Text");
        r2.setTop("50");
        r2.setLeft("0");
        r2.setHeight("50");
        r2.setWidth("100");
        r2.setFit("hidden");
        List<SmilHeadLayoutRootLayoutHeadRegion> regions = new ArrayList<SmilHeadLayoutRootLayoutHeadRegion>();
        regions.add(r1);
        regions.add(r2);

        SmilHeadLayoutRootLayout rootLayout = new SmilHeadLayoutRootLayout(regions);
        rootLayout.setHeight("208");
        rootLayout.setWidth("176");

        SmilHeadLayout headLayout = new SmilHeadLayout(rootLayout);
        SmilHead smilHead = new SmilHead(headLayout);


        SmilBodyParImg img1 = new SmilBodyParImg();
        img1.setRegion("Image");
        img1.setSrc("1.jpg");
        SmilBodyParTxt txt1 = new SmilBodyParTxt();
        txt1.setRegion("Text");
        txt1.setSrc("2.txt");
        SmilBodyPar p1 = new SmilBodyPar(img1, txt1);
        p1.setDur("2000ms");

        SmilBodyParImg img3 = new SmilBodyParImg();
        img3.setRegion("Image");
        img3.setSrc("3.jpg");
        SmilBodyPar p2 = new SmilBodyPar(img3);
        p2.setDur("3000ms");

        List<SmilBodyPar> pars = new ArrayList<SmilBodyPar>();
        pars.add(p1);
        pars.add(p2);
        SmilBody body = new SmilBody(pars);

        Smil smilInfo = new Smil(smilHead, body);
        smilInfo.setXmlns("http://www.w3.org/2000/SMIL20/CR/Language");
        String str = null;
        try {
            str = convertToXml(smilInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(str);


//        SmilInfo s = new SmilInfo();
//        s.setXmlns("http://www.w3.org/2000/SMIL20/CR/Language");
//        String str = convertToXml(s);
//        System.out.println(str);

//        Desk d = new Desk();
//        d.setId("1231");
//        String str = convertToXml(d);
//        System.out.println(str);
    }
}
