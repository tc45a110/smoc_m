package com.smoc.cloud.scheduler.filters.service.utils.DFA;

import java.util.*;

/**
 * 基于DFA超级白词过滤器（系统超级白词）
 */
public class WordsSuperWhiteFilter {

    @SuppressWarnings("rawtypes")
    private Map superWhiteWordsMap = null;

    //最小匹配规则
    public static int minMatchTYpe = 1;
    //最大匹配规则
    public static int maxMatchType = 2;

    /**
     * 构造函数
     */
    public WordsSuperWhiteFilter() {
    }

    /**
     * 判断文字是否包含超级白词字符
     *
     * @param message   内容
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     */
    public boolean isContain(String message, int matchType) {
        boolean flag = false;
        if (null == superWhiteWordsMap || superWhiteWordsMap.size() < 0) {
            return flag;
        }
        for (int i = 0; i < message.length(); i++) {
            //判断是否包含超级白字符
            int matchFlag = this.checkSuperWhiteWord(message, i, matchType);
            //大于0存在，返回true
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的超级白词
     *
     * @param message   内容
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public Set<String> getSuperWhiteWords(String message, int matchType) {
        Set<String> superWhiteWordList = new HashSet<String>();
        if (null == superWhiteWordsMap || superWhiteWordsMap.size() < 0) {
            return superWhiteWordList;
        }
        for (int i = 0; i < message.length(); i++) {
            int length = checkSuperWhiteWord(message, i, matchType);    //判断是否包含字符
            if (length > 0) {    //存在,加入list中
                superWhiteWordList.add(message.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return superWhiteWordList;
    }

    /**
     * 替换超级白词字符
     *
     * @param message     内容
     * @param matchType   匹配规则 1：最小匹配规则，2：最大匹配规则
     * @param replaceChar 替换字符，默认*
     */
    public String replaceSensitiveWords(String message, int matchType, String replaceChar) {
        String resultTxt = message;
        Set<String> set = getSuperWhiteWords(message, matchType);     //获取所有的超级白词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar 替换字符
     * @param length
     * @return
     */
    private String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含超级白词字符，检查规则如下：
     *
     * @param message    内容
     * @param beginIndex
     * @param matchType
     * @return，如果存在，则返回超级白词字符的长度，不存在返回0
     */
    @SuppressWarnings({"rawtypes"})
    public int checkSuperWhiteWord(String message, int beginIndex, int matchType) {
        boolean flag = false;    //超级白词结束标识位：用于超级白词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = superWhiteWordsMap;
        //message = BCConvert.qj2bj(message);
        for (int i = beginIndex; i < message.length(); i++) {
            word = message.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (WordsSuperWhiteFilter.minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }


    /**
     * 读取超级白词库，将超级白词放入HashSet中，构建一个DFA算法模型：
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initializeSuperWhiteWords(Set<String> keyWords) {
        superWhiteWordsMap = new HashMap(keyWords.size());     //初始化超级白词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWords.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = superWhiteWordsMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
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
    }

    public static void main(String[] args) {
        WordsSuperWhiteFilter filter = new WordsSuperWhiteFilter();
        System.out.println("超级白词的数量：" + filter.superWhiteWordsMap.size());
        String string = "事件婊子太多的伤感情怀也许只局限于 不良少女日记荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        Set<String> set = filter.getSuperWhiteWords(string, 1);
        long endTime = System.currentTimeMillis();
        System.out.println("语句中包含超级白词的个数为：" + set.size() + "。包含：" + set);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }
}