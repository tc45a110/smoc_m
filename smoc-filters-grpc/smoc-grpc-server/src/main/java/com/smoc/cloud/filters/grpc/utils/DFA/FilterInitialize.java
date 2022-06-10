package com.smoc.cloud.filters.grpc.utils.DFA;

import java.util.HashMap;
import java.util.Map;

public class FilterInitialize {

    //系统敏感词次DAF过滤
    public static WordsSensitiveFilter sensitiveWordsFilter = new WordsSensitiveFilter();

    //系统审核次DAF过滤
    public static WordsCheckFilter checkWordsFilter = new WordsCheckFilter();

    //系统超级词次DAF过滤
    public static WordsSuperWhiteFilter superWhiteWordsFilter = new WordsSuperWhiteFilter();

    //存放加载的行业敏感词 及构造完成的DFA算法map
    public static Map<String, Map> infoTypeSensitiveMap = new HashMap();

    //存放加载的通道敏感词 及构造完成的DFA算法map
    public static Map<String, Map> channelSensitiveMap = new HashMap();

    //存放加载的业务账号敏感词 及构造完成的DFA算法map
    public static Map<String, Map> accountSensitiveMap = new HashMap();

    //存放加载的业务账号审核词 及构造完成的DFA算法map
    public static Map<String, Map> accountCheckMap = new HashMap();

    //存放加载的业务账号超级白词 及构造完成的DFA算法map
//    public static Map<String, Map> accountSuperWhiteMap = new HashMap();

    //存放加载的业务账号固定模版，匹配后要跳过其他内容过滤
    public static Map<String, String> accountFilterFixedTemplateMap = new HashMap();

    //存放加载的业务账号变量模版，匹配后要继续过滤
    public static Map<String, String> accountFilterVariableTemplateMap = new HashMap();

    //存放加载的业务账号变量模版，匹配后要跳过其他内容过滤
    public static Map<String, String> accountNoFilterVariableTemplateMap = new HashMap();

    //存放加载的业务账号签名模版
    public static Map<String, String> accountSignTemplateMap = new HashMap();

}
