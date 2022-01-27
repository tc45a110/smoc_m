package com.smoc.cloud.identification.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class HttpIdentityIdNumberNameTest {

   public  static void main(String[] args) throws Exception {
       String url = "http://api.faceos.com/openapi/IdNameCheck?appKey=xwRfbS8LIh&appScrect=804f7493a6622482b5f03560d3772d1f";
       Map<String,String> map = new HashMap<>();
       map.put("name","武基慧");
       map.put("cardNo","372523198002165313");
       String jsonData = new Gson().toJson(map);
       String result = Okhttp3Utils.postJson(url,jsonData,new HashMap<>());
   }
}
