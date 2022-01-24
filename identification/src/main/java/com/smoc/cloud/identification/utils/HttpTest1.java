package com.smoc.cloud.identification.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class HttpTest1 {

   public  static void main(String[] args) throws Exception {
       String url = "http://api.faceos.com/openapi/IdNamePhotoCheck?appKey=xwRfbS8LIh&appScrect=804f7493a6622482b5f03560d3772d1f";


       Map<String,String> map = new HashMap<>();
       map.put("name","武基慧");
       map.put("cardNo","372523198002165313");
       map.put("faceBase64",ImageUtils.getImgFileToBase64("/Users/wujihui/Desktop/smoc4.0/WechatIMG3658.jpeg"));
       String jsonData = new Gson().toJson(map);
       //log.info(jsonData);
       String result = Okhttp3Utils.postJson(url,jsonData,new HashMap<>());
       log.info(result);
   }
}
