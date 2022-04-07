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
 * 系统黑词、检查词、白词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class SystemKeyWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(SystemKeyWordsFilter.class.toString());

    public SystemKeyWordsFilter(LoadDataService loadDataService) {
        this.systemBlackWordsPattern = loadDataService.getSystemBlackWords();
        this.systemCheckWordsPattern = loadDataService.getSystemCheckWords();
        this.systemWhiteWordsPattern = loadDataService.getSystemWhiteWords();
    }

    //系统黑词  Pattern
    private Pattern systemBlackWordsPattern;

    //系统审核词 Pattern
    private Pattern systemCheckWordsPattern;

    //系统白词 Pattern
    private Pattern systemWhiteWordsPattern;

    /**
     * 系统黑词、检查词、白词过滤
     *
     * @param phone        手机号
     * @param message      短消息内容
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    @Override
    public void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain) {

        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0) {
            chain.doFilter(phone, message, filterResult, chain);
            return;
        }

        //检查黑词
        if (null != systemBlackWordsPattern) {
            Matcher matcher = systemBlackWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.put(Constant.SYSTEM_KEY_WORDS_FILTER, "black");
            }
        }

        //检查审核词
        if (null != systemCheckWordsPattern) {
            Matcher matcher = systemCheckWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.put(Constant.SYSTEM_KEY_WORDS_FILTER, "check");
            }
        }

        //用系统白词，尝试洗白黑词
        if (null != systemWhiteWordsPattern && "black".equals(filterResult.get(Constant.SYSTEM_KEY_WORDS_FILTER))) {
            Matcher matcher = systemWhiteWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.remove(Constant.SYSTEM_KEY_WORDS_FILTER);
            }
        }

        logger.info("[Filters]:系统关键词过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }
}
