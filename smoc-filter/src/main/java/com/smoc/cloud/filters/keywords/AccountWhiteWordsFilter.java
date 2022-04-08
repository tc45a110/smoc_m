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
 * 业务账号白词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class AccountWhiteWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(AccountWhiteWordsFilter.class.toString());

    private LoadDataService loadDataService;

    private String account;

    public AccountWhiteWordsFilter(LoadDataService loadDataService, String account) {
        this.loadDataService = loadDataService;
        this.account = account;
    }

    //业务账号白词 Pattern
    private Pattern accountWhiteWordsPattern;

    /**
     * 业务账号黑词、检查词、白词过滤
     *
     * @param phone        手机号
     * @param message      短消息内容
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    @Override
    public void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain) {


        //判断是否有要洗的黑词
        if(!("black".equals(filterResult.get(Constant.SYSTEM_BLACK_WORDS_FILTER)) || "black".equals(filterResult.get(Constant.ACCOUNT_BLACK_WORDS_FILTER)))){
            chain.doFilter(phone, message, filterResult, chain);
            return;
        }

        this.accountWhiteWordsPattern = loadDataService.getAccountWhiteWords(account);

        //如果有系统黑词，尝试黑词洗白
        if (null == filterResult || filterResult.size() > 0) {
            if (null != accountWhiteWordsPattern && ("black".equals(filterResult.get(Constant.SYSTEM_BLACK_WORDS_FILTER)))) {
                Matcher matcher = accountWhiteWordsPattern.matcher(message);
                if (matcher.find()) {
                    filterResult.remove(Constant.SYSTEM_BLACK_WORDS_FILTER);
                }
            }
        }

        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0) {
            chain.doFilter(phone, message, filterResult, chain);
            return;
        }

        //用业务账号白词，尝试洗白黑词
        if (null != accountWhiteWordsPattern && "black".equals(filterResult.get(Constant.ACCOUNT_BLACK_WORDS_FILTER))) {
            Matcher matcher = accountWhiteWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.remove(Constant.ACCOUNT_BLACK_WORDS_FILTER);
            }
        }

        logger.info("[Filters]:业务账号白词过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }

}
