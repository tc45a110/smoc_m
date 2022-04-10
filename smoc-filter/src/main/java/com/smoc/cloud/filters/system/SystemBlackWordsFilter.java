package com.smoc.cloud.filters.system;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统黑词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class SystemBlackWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(SystemBlackWordsFilter.class.toString());

    //系统黑词过滤器
    public static final String FILTER_KEY = Constant.SYSTEM_BLACK_WORDS_FILTER;


    /**
     * 系统黑词、检查词、白词过滤
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

        Pattern systemBlackWordsPattern = loadDataService.getSystemBlackWords(params.getAccount());

        //检查黑词
        if (null != systemBlackWordsPattern) {
            //logger.info("[message]:"+params.getMessage());
            Matcher matcher = systemBlackWordsPattern.matcher(params.getMessage());
            if (matcher.find()) {
                filterResult.put(FILTER_KEY, "black");
            }
        }

        //logger.info("[message]:"+params.getMessage());

        //logger.info("[Filters]:系统黑词过滤");
        chain.doFilter(params,loadDataService, filterResult, chain);
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
