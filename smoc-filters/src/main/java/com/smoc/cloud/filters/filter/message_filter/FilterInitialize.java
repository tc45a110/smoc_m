package com.smoc.cloud.filters.filter.message_filter;

import java.util.Set;

public class FilterInitialize {

    public static SensitiveWordsFilter sensitiveWordsFilter = new SensitiveWordsFilter();
    public static void init(Set<String> sensitiveWords){
        System.out.println(sensitiveWords);
        sensitiveWordsFilter.initializeSensitiveWords(sensitiveWords);
    }
}
