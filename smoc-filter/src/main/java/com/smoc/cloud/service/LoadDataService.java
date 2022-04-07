package com.smoc.cloud.service;

import java.util.regex.Pattern;

/**
 * 消息发送验证，数据接口
 */
public interface LoadDataService {

    /**
     * 获取系统关键词-黑词
     * 描述：获取系统关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-黑词组成的 Pattern
     */
    Pattern getSystemBlackWords();

    /**
     * 获取系统关键词-审核词
     * 描述：获取系统关键词-审核词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-审核词组成的 Pattern
     */
    Pattern getSystemCheckWords();
    /**
     * 获取系统关键词-白词
     * 描述：获取系统关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-白词组成的 Pattern
     */
    Pattern getSystemWhiteWords();
    /**
     * 获取业务账号关键词-黑词
     * 描述：获取业务账号关键词-黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 业务账号关键字-黑词组成的 Pattern
     */
    Pattern getAccountBlackWords(String account);

    /**
     * 获取业务账号关键词-审核词
     * 描述：获取业务账号关键词-审核词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 业务账号关键字-审核词组成的 Pattern
     */
    Pattern getAccountCheckWords(String account);

    /**
     * 获取业务账号关键词-白词
     * 描述：获取业务账号关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 业务账号关键字-白词组成的 Pattern
     */
    Pattern getAccountWhiteWords(String account);

    /**
     * 获取通道关键词-黑词
     * 描述：获取通道关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-黑词组成的 Pattern
     */
    Pattern getChannelBlackWords(String channelId);

    /**
     * 获取通道关键词-审核词
     * 描述：获取通道关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-审核词组成的 Pattern
     */
    Pattern getChannelCheckWords(String channelId);

    /**
     * 获取通道关键词-白词
     * 描述：获取通道关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-白词组成的 Pattern
     */
    Pattern getChannelWhiteWords(String channelId);

    /**
     * 获取运营商关键词-黑词
     * 描述：获取运营商关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-黑词组成的 Pattern
     */
    Pattern getCarrierBlackWords(String carrier);

    /**
     * 获取运营商关键词-审核词
     * 描述：获取运营商关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-审核词组成的 Pattern
     */
    Pattern getCarrierCheckWords(String carrier);

    /**
     * 获取运营商关键词-白词
     * 描述：获取运营商关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-白词组成的 Pattern
     */
    Pattern getCarrierWhiteWords(String carrier);

    /**
     * 获取信息分类关键词-黑词
     * 描述：获取信息分类关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-黑词组成的 Pattern
     */
    Pattern getInfoTypeBlackWords(String infoType);

    /**
     * 获取信息分类关键词-审核词
     * 描述：获取信息分类关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-审核词组成的 Pattern
     */
    Pattern getInfoTypeCheckWords(String infoType);

    /**
     * 获取信息分类关键词-白词
     * 描述：获取信息分类关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-白词组成的 Pattern
     */
    Pattern getInfoTypeWhiteWords(String infoType);
}
