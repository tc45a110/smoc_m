package com.smoc.cloud.filters.utils.DFA;

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

    //存放加载的业务账号敏感词 及构造完成的DFA算法map
    public static Map<String, Map> accountSensitiveMap = new HashMap();

}
