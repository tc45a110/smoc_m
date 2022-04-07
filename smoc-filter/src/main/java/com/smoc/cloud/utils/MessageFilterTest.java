package com.smoc.cloud.utils;

import com.smoc.cloud.service.FilterService;
import com.smoc.cloud.service.LoadDataServiceImpl;

import java.util.Map;

public class MessageFilterTest {


    public static void main(String[] args) {
        FilterService service = new FilterService(new LoadDataServiceImpl());
        Map<String, String> resultMap = service.validateMessage("CHID100056", "IOC102", "UNIC", "INDUSTRY", "18510256887", "对方对方的jdjfd国民党");

        if (resultMap.size() > 0) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        } else {
            System.out.println("通过校验");
        }
    }

}
