package com.smoc.cloud.filters.grpc.filters.utils.DFA;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于DFA敏感词过滤器
 */
public class DfaSensitiveWordsFilter {

    @SuppressWarnings("rawtypes")
    private Map dfaDataMap = null;

    //最小匹配规则
    public static int minMatchTYpe = 1;
    //最大匹配规则
    public static int maxMatchType = 2;

    /**
     * 构造函数
     */
    public DfaSensitiveWordsFilter(Map dfaDataMap) {

        this.dfaDataMap = dfaDataMap;
    }

    /**
     * 判断文字是否包含敏感词
     *
     * @param phone     手机号
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     */
    public boolean isContain(String phone, int matchType) {
        boolean flag = false;
        if (null == dfaDataMap || dfaDataMap.size() < 0) {
            return flag;
        }
        for (int i = 0; i < phone.length(); i++) {
            //判断是否包含黑名单
            int matchFlag = this.checkSensitiveWords(phone, i, matchType);
            //大于0存在，返回true
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param message     手机号
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public Set<String> getSensitiveWords(String message, int matchType) {
        Set<String> superWhiteWordList = new HashSet<String>();
        if (null == dfaDataMap || dfaDataMap.size() < 0) {
            return superWhiteWordList;
        }
        for (int i = 0; i < message.length(); i++) {
            int length = checkSensitiveWords(message, i, matchType);    //判断是否包含字符
            if (length > 0) {    //存在,加入list中
                superWhiteWordList.add(message.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return superWhiteWordList;
    }

    /**
     * 检查文字中是否包含敏感词字符，检查规则如下：
     *
     * @param phone      手机号
     * @param beginIndex
     * @param matchType
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     */
    @SuppressWarnings({"rawtypes"})
    public int checkSensitiveWords(String phone, int beginIndex, int matchType) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = dfaDataMap;
        //phone = BCConvert.qj2bj(phone);
        for (int i = beginIndex; i < phone.length(); i++) {
            word = phone.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (DfaSensitiveWordsFilter.minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
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

    public Map getDfaDataMap() {
        return dfaDataMap;
    }

    public static void main(String[] args) {

    }
}