package com.smoc.cloud.filters.keywords;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.model.ParamModel;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务账号检查词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class AccountCheckWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(AccountCheckWordsFilter.class.toString());

    public static final String FILTER_KEY = Constant.ACCOUNT_CHECK_WORDS_FILTER;

    /**
     * 业务账号黑词、检查词、白词过滤
     *
     * @param params       参数对象
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    @Override
    public void doFilter(ParamModel params,LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain){

        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0) {
            chain.doFilter(params,loadDataService, filterResult, chain);
            return;
        }

        Pattern accountCheckWordsPattern = loadDataService.getAccountCheckWords(params.getAccount());

        //检查审核词
        if (null != accountCheckWordsPattern) {
            Matcher matcher = accountCheckWordsPattern.matcher(params.getMessage());
            if (matcher.find()) {
                filterResult.put(Constant.ACCOUNT_CHECK_WORDS_FILTER, "check");
            }
        }

        //logger.info("[Filters]:业务账号审核词过滤");
        chain.doFilter(params,loadDataService, filterResult, chain);
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

}
