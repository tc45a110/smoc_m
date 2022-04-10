package com.smoc.cloud.utils;

import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.service.FilterService;
import com.smoc.cloud.service.LoadDataServiceImpl;

import java.util.Map;

public class MessageFilterTest {


    public static void main(String[] args) {

        FilterService service = new FilterService(new LoadDataServiceImpl());
        ParamModel params = new ParamModel();
        params.setPhone("18510256887");
        params.setMessage("对方对方的jdjfd国民党");
        params.setAccount("IOC102");
        params.setCarrier("UNIC");
        params.setChannelId("CHID100056");
        params.setProvince("003");
        params.setInfoType("INDUSTRY");

        Long start = System.currentTimeMillis();
        System.out.println("-----start-------" + start);
        for (int i = 0; i < 100; i++) {

            Map<String, String> resultMap = service.validateMessage(params);

//            if (resultMap.size() > 0) {
//                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
//                    System.out.println(entry.getKey() + ":" + entry.getValue());
//                }
//            } else {
//                System.out.println("通过校验");
//            }
        }
        Long end = System.currentTimeMillis();
        System.out.println("-----e n d-------" + end);
        System.out.println("耗时："+(end-start)+"毫秒");


    }

}
