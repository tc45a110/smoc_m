package com.smoc.examples.filters.example;

import com.google.gson.Gson;
import com.smoc.examples.utils.DateTimeUtils;
import com.smoc.examples.utils.Okhttp3Utils;
import com.smoc.examples.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证过滤 示例
 */
public class FilterExample {

    public static void main(String[] args) throws Exception {
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 2000; i++) {

            //请求路径（具体参见技术文档）
            String url = "http://localhost:18090/smoc-filters/message-filter/filters";

            //自定义header协议
            Map<String, String> header = new HashMap<>();
            //
            Map<String, Object> data = new HashMap<>();
            data.put("phone", "19918876655");
            data.put("account", "YQT124");
            data.put("carrier", "TELC");
            String message = "事件婊子太多的伤感情怀也许只局限于 不良少女日记荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                    + "然后法轮功我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                    + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 乳方 深人静的晚上，关上电话静静的发呆着。";
            data.put("message", message);
            data.put("numbers", 1);

            //转JSON请求数据
            String requestJsonData = new Gson().toJson(data);

            String result = Okhttp3Utils.postJson(url, requestJsonData, header);
            //Thread.currentThread().sleep(1000);
            //System.out.println(System.currentTimeMillis()+"[请求响应]数据:" + result);
        }
        System.out.println(System.currentTimeMillis());
    }
}
