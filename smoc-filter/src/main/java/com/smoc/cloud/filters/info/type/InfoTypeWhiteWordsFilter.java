package com.smoc.cloud.filters.info.type;

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
 * 信息分类白词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class InfoTypeWhiteWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(InfoTypeWhiteWordsFilter.class.toString());

    public static final String FILTER_KEY = Constant.INFO_TYPE_WHITE_WORDS_FILTER;

    /**
     * 运营商黑词、检查词、白词过滤
     *
     * @param params       参数对象
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    @Override
    public void doFilter(ParamModel params,LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain){

        //判断是否有要洗的黑词
        if(!"black".equals(filterResult.get(Constant.INFO_TYPE_BLACK_WORDS_FILTER))){
            chain.doFilter(params,loadDataService, filterResult, chain);
            return;
        }

        Pattern infoTypeWhiteWordsPattern = loadDataService.getInfoTypeWhiteWords(params.getInfoType());

        //用信息分类白词，尝试洗白黑词
        if (null != infoTypeWhiteWordsPattern) {
            Matcher matcher = infoTypeWhiteWordsPattern.matcher(params.getMessage());
            if (matcher.find()) {
                filterResult.remove(Constant.INFO_TYPE_BLACK_WORDS_FILTER);
            }
        }

        //logger.info("[Filters]:信息分类白词过滤");
        chain.doFilter(params,loadDataService, filterResult, chain);
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }

}
