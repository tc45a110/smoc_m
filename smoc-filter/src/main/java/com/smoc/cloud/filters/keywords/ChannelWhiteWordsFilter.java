package com.smoc.cloud.filters.keywords;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通道白词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class ChannelWhiteWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(ChannelWhiteWordsFilter.class.toString());

    private LoadDataService loadDataService;

    private String channelId;

    public ChannelWhiteWordsFilter(LoadDataService loadDataService, String channelId) {
        this.loadDataService = loadDataService;
        this.channelId = channelId;

    }

    //业务账号白词 Pattern
    private Pattern channelWhiteWordsPattern;

    /**
     * 通道黑词、检查词、白词过滤
     *
     * @param phone        手机号
     * @param message      短消息内容
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    @Override
    public void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain) {

        //判断是否有要洗的黑词
        if(!"black".equals(filterResult.get(Constant.CHANNEL_BLACK_WORDS_FILTER))){
            chain.doFilter(phone, message, filterResult, chain);
            return;
        }

        this.channelWhiteWordsPattern = loadDataService.getChannelWhiteWords(channelId);

        //用通道白词，洗白黑词
        if (null != channelWhiteWordsPattern) {
            Matcher matcher = channelWhiteWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.remove(Constant.CHANNEL_BLACK_WORDS_FILTER);
            }
        }

        logger.info("[Filters]:通道白词过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }

}
