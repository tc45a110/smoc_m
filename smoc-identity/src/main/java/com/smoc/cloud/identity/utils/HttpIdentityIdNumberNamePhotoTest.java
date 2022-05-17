package com.smoc.cloud.identity.utils;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpIdentityIdNumberNamePhotoTest {

   public  static void main(String[] args) throws Exception {
       //String url = "http://api.faceos.com/openapi/IdNamePhotoCheck?appKey=xwRfbS8LIh&appScrect=804f7493a6622482b5f03560d3772d1f";


//       Map<String,String> map = new HashMap<>();
//       map.put("name","武基慧");
//       map.put("cardNo","372523198002165313");
//       map.put("faceBase64",ImageUtils.getImgFileToBase64("/Users/wujihui/Desktop/smoc4.0/WechatIMG3658.jpeg"));
//       String jsonData = new Gson().toJson(map);
//       //log.info(jsonData);
//       String result = Okhttp3Utils.postJson(url,jsonData,new HashMap<>());
       //jxDd/5elG3VmZ3sWfSpMLpikcjF2Kp7+OLEHbA6xeAA=
       //jxDd/5elG3VmZ3sWfSpMLpikcjF2Kp7+OLEHbA6xeAA=
       String cardNo = AESUtil.encrypt("372523198002165313","fNT44A1FM57Wu8HF5D7C6bHX372699MP","E96nx1xL929I86Te");
       log.info(cardNo);
   }
}
