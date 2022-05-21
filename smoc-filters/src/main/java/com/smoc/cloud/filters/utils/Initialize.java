package com.smoc.cloud.filters.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Initialize {


    public static Set<String> sensitiveWords;

    static {
        sensitiveWords = new HashSet<>();
        sensitiveWords.add("推油");
        sensitiveWords.add("胸推");
        sensitiveWords.add("狼友");
        sensitiveWords.add("吞精");
        sensitiveWords.add("咪咪");
        sensitiveWords.add("婊子");
        sensitiveWords.add("乳方");
        sensitiveWords.add("尼玛");
        sensitiveWords.add("全职");
    }
}
