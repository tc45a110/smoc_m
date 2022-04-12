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
     * @param account 根据account判定是否过滤关键字，不过滤返回null
     * @return Pattern 系统关键字-黑词组成的 Pattern
     */
    Pattern getSystemBlackWords(String account);

    /**
     * 获取系统关键词-审核词
     * 描述：获取系统关键词-审核词；当黑词为空时候，直接返回null；
     *
     * @param account 根据account判定是否过滤关键字，不过滤返回null
     * @return Pattern 系统关键字-审核词组成的 Pattern
     */
    Pattern getSystemCheckWords(String account);

    /**
     * 获取系统关键词-白词
     * 描述：获取系统关键词-白词；当白词为空时候，直接返回null；
     *
     * @param account 根据account判定是否过滤关键字，不过滤返回null
     * @return Pattern 系统关键字-白词组成的 Pattern
     */
    Pattern getSystemWhiteWords(String account);

    /**
     * 获取业务账号关键词-黑词
     * 描述：获取业务账号关键词-黑词；当黑词为空时候，直接返回null；
     *
     * @param account 根据account 加载account设置的关键词
     * @return Pattern 业务账号关键字-黑词组成的 Pattern
     */
    Pattern getAccountBlackWords(String account);

    /**
     * 获取业务账号关键词-审核词
     * 描述：获取业务账号关键词-审核词；当黑词为空时候，直接返回null；
     *
     * @param account 根据account 加载account设置的关键词
     * @return Pattern 业务账号关键字-审核词组成的 Pattern
     */
    Pattern getAccountCheckWords(String account);

    /**
     * 获取业务账号关键词-白词
     * 描述：获取业务账号关键词-白词；当白词为空时候，直接返回null；
     *
     * @param account 根据account 加载account设置的关键词
     * @return Pattern 业务账号关键字-白词组成的 Pattern
     */
    Pattern getAccountWhiteWords(String account);

    /**
     * 获取通道关键词-黑词
     * 描述：获取通道关键词黑词；当黑词为空时候，直接返回null；
     *
     * @param channelId 通道ID,根据channelId加载设置的关键词
     * @return Pattern 通道关键字-黑词组成的 Pattern
     */
    Pattern getChannelBlackWords(String channelId);

    /**
     * 获取通道关键词-审核词
     * 描述：获取通道关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @param channelId 通道ID,根据channelId加载设置的关键词
     * @return Pattern 通道关键字-审核词组成的 Pattern
     */
    Pattern getChannelCheckWords(String channelId);

    /**
     * 获取通道关键词-白词
     * 描述：获取通道关键词-白词；当白词为空时候，直接返回null；
     *
     * @param channelId 通道ID,根据channelId加载设置的关键词
     * @return Pattern 通道关键字-白词组成的 Pattern
     */
    Pattern getChannelWhiteWords(String channelId);

    /**
     * 获取运营商关键词-黑词
     * 描述：获取运营商关键词黑词；当黑词为空时候，直接返回null；
     *
     * @param carrier 运营商，根据carrier加载运营商设置的关键词
     * @return Pattern 运营商关键字-黑词组成的 Pattern
     */
    Pattern getCarrierBlackWords(String carrier);

    /**
     * 获取运营商关键词-审核词
     * 描述：获取运营商关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @param carrier 运营商，根据carrier加载运营商设置的关键词
     * @return Pattern 运营商关键字-审核词组成的 Pattern
     */
    Pattern getCarrierCheckWords(String carrier);

    /**
     * 获取运营商关键词-白词
     * 描述：获取运营商关键词-白词；当白词为空时候，直接返回null；
     *
     * @param carrier 运营商，根据carrier加载运营商设置的关键词
     * @return Pattern 运营商关键字-白词组成的 Pattern
     */
    Pattern getCarrierWhiteWords(String carrier);

    /**
     * 获取信息分类关键词-黑词
     * 描述：获取信息分类关键词黑词；当黑词为空时候，直接返回null；
     *
     * @param infoType 信息分类；根据infoType加载设置的关键词
     * @return Pattern 信息分类关键字-黑词组成的 Pattern
     */
    Pattern getInfoTypeBlackWords(String infoType);

    /**
     * 获取信息分类关键词-审核词
     * 描述：获取信息分类关键词-审核词；当审核此为空时候，直接返回null；
     *
     * @param infoType 信息分类；根据infoType加载设置的关键词
     * @return Pattern 信息分类关键字-审核词组成的 Pattern
     */
    Pattern getInfoTypeCheckWords(String infoType);

    /**
     * 获取信息分类关键词-白词
     * 描述：获取信息分类关键词-白词；当白词为空时候，直接返回null；
     *
     * @param infoType 信息分类；根据infoType加载设置的关键词
     * @return Pattern 信息分类关键字-白词组成的 Pattern
     */
    Pattern getInfoTypeWhiteWords(String infoType);

    /**
     * 判断业务账号指定运营商的日限量，可以发返回true；不可以发返回false，不过滤返回null
     *
     * @param account 业务账号
     * @param carrier 运营商
     * @return
     */
    Boolean validateAccountDailyLimit(String account, String carrier);

    /**
     * 判定现在时间内是否可以发送；返回true表示可以发；返回false表示不可以发；返回null 表示不过滤
     *
     * @param account 业务账号
     * @return
     */
    Boolean validateAccountTimeLimit(String account);

    /**
     * 判定是否进行了省份限制，返回true表示可以发；返回false表示不可以发；返回null 表示不过滤
     *
     * @param account  业务账号
     * @param carrier  运营商
     * @param province 省份
     * @return
     */
    Boolean validateProvinceLimit(String account, String carrier, String province);

    /**
     * 判定账号，单个手机号发送频次，返回true表示可以发；返回false表示不可以发；返回null 表示不过滤
     *
     * @param account 业务账号
     * @param phone   手机号
     * @return
     */
    Boolean validateAccountSendFrequency(String account, String phone);

    /**
     * 判定手机号是否在黑名单，返回true表示表示存在黑名单中；返回false表示不存在黑名单中；返回null 表示不过滤
     *
     * @param phone 手机号
     * @return
     */
    Boolean isExistSystemBlackList(String phone);

    /**
     * 前提是，这个手机好已经在黑名单了，现在判断下，这个手机号是否在白名单，在得化，则洗白这个号码
     * 判定手机号是否在白名单，返回true表示存在；返回false表示不存在；在白名单，则会洗白这个在黑名单得手机号
     *
     * @param phone 手机号
     * @return
     */
    Boolean isExistSystemWhiteList(String phone);

    /**
     * 验证签名发送频率限制
     * 两个层次一个是 系统层次验证，一个是 业务账号层次验证
     * 可以发则返回true，收到限制，则返回false；返回null 则不执行过滤
     *
     * @param account 业务账号
     * @param sign    签名
     * @return
     */
    Boolean validateSignLimit(String account, String sign);


}
