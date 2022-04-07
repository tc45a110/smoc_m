package com.smoc.cloud.service;

import java.util.regex.Pattern;

/**
 * 过滤器 加载数据
 */
public class LoadDataServiceImpl implements LoadDataService {


    /**
     * 获取系统关键词-黑词
     * 描述：获取系统关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getSystemBlackWords() {

        Pattern blackWordsPattern = Pattern.compile("aa|bb");
        return blackWordsPattern;
    }

    /**
     * 获取系统关键词-审核词
     * 描述：获取系统关键词-审核词；当审核词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-审核词组成的 Pattern
     */
    @Override
    public Pattern getSystemCheckWords() {

        Pattern checkWordsPattern = Pattern.compile("aa|bb");
        return checkWordsPattern;

    }

    /**
     * 获取系统关键词-白词
     * 描述：获取系统关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 系统关键字-白词组成的 Pattern
     */
    @Override
    public Pattern getSystemWhiteWords() {
        return null;
    }

    /**
     * 获取业务账号关键词-黑词
     * 描述：获取业务账号关键词-黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 业务账号关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getAccountBlackWords(String account) {

        Pattern blackWordsPattern = Pattern.compile("aa|bb");
        return blackWordsPattern;

    }

    /**
     * 获取业务账号关键词-审核词
     * 描述：获取业务账号关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 业务账号关键字-审核词组成的 Pattern
     */
    @Override
    public Pattern getAccountCheckWords(String account) {

        Pattern checkWordsPattern = Pattern.compile("aa|bb");
        return checkWordsPattern;

    }

    /**
     * 获取通道关键词-黑词
     * 描述：获取通道关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getAccountWhiteWords(String account) {

        Pattern whiteWordsPattern = Pattern.compile("aa|bb");
        return whiteWordsPattern;

    }

    /**
     * 获取通道关键词-黑词
     * 描述：获取通道关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getChannelBlackWords(String channelId) {

        Pattern blackWordsPattern = Pattern.compile("aa|bb");
        return blackWordsPattern;

    }

    /**
     * 获取通道关键词-审核词
     * 描述：获取通道关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-审核词组成的 Pattern
     */
    @Override
    public Pattern getChannelCheckWords(String channelId) {

        Pattern checkWordsPattern = Pattern.compile("aa|bb");
        return checkWordsPattern;

    }

    /**
     * 获取通道关键词-白词
     * 描述：获取通道关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 通道关键字-白词组成的 Pattern
     */
    @Override
    public Pattern getChannelWhiteWords(String channelId) {

        Pattern whiteWordsPattern = Pattern.compile("aa|bb");
        return whiteWordsPattern;

    }

    /**
     * 获取运营商关键词-黑词
     * 描述：获取运营商关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getCarrierBlackWords(String carrier) {

        Pattern blackWordsPattern = Pattern.compile("aa|bb");
        return blackWordsPattern;

    }

    /**
     * 获取运营商关键词-审核词
     * 描述：获取运营商关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-审核词组成的 Pattern
     */
    @Override
    public Pattern getCarrierCheckWords(String carrier) {

        Pattern checkWordsPattern = Pattern.compile("aa|bb");
        return checkWordsPattern;

    }

    /**
     * 获取运营商关键词-白词
     * 描述：获取运营商关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 运营商关键字-白词组成的 Pattern
     */
    @Override
    public Pattern getCarrierWhiteWords(String carrier) {

        Pattern whiteWordsPattern = Pattern.compile("aa|bb");
        return whiteWordsPattern;

    }

    /**
     * 获取信息分类关键词-黑词
     * 描述：获取信息分类关键词黑词；当黑词为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-黑词组成的 Pattern
     */
    @Override
    public Pattern getInfoTypeBlackWords(String infoType) {

        Pattern blackWordsPattern = Pattern.compile("aa|国民党");
        return blackWordsPattern;

    }

    /**
     * 获取信息分类关键词-审核词
     * 描述：获取信息分类关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-审核词组成的 Pattern
     */
    @Override
    public Pattern getInfoTypeCheckWords(String infoType) {

        Pattern checkWordsPattern = Pattern.compile("aa|bb");
        return checkWordsPattern;

    }

    /**
     * 获取信息分类关键词-白词
     * 描述：获取信息分类关键词-白词；当白词为空时候，直接返回null；
     *
     * @return Pattern 信息分类关键字-白词组成的 Pattern
     */
    @Override
    public Pattern getInfoTypeWhiteWords(String infoType) {

        Pattern whiteWordsPattern = Pattern.compile("aa|bb");
        return whiteWordsPattern;

    }
}
