package com.smoc.cloud.scheduler.service.filters.utils.DFA;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DFAUtils {

    /**
     * 构建DFA算法数据模型
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map buildDFADataModel(Set<String> keyWords) {
        Map dfaDataMap = new HashMap(keyWords.size());     //初始化系统黑名单容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWords.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = dfaDataMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) { //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else { //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");  //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1"); //最后一个
                }
            }
        }

        return dfaDataMap;
    }
}
