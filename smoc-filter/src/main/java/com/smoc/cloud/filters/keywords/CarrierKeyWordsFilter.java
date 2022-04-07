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
 * 运营商黑词、检查词、白词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class CarrierKeyWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(CarrierKeyWordsFilter.class.toString());

    public CarrierKeyWordsFilter(LoadDataService loadDataService,String carrier){
        this.carrierBlackWordsPattern = loadDataService.getCarrierBlackWords(carrier);
        this.carrierCheckWordsPattern = loadDataService.getCarrierCheckWords(carrier);
        this.carrierWhiteWordsPattern = loadDataService.getCarrierWhiteWords(carrier);
    }

    //业务账号黑词  Pattern
    private Pattern carrierBlackWordsPattern;

    //业务账号审核词 Pattern
    private Pattern carrierCheckWordsPattern;

    //业务账号白词 Pattern
    private Pattern carrierWhiteWordsPattern;

    /**
     * 运营商黑词、检查词、白词过滤
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
        if (null != carrierBlackWordsPattern) {
            Matcher matcher = carrierBlackWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.put(Constant.CARRIER_KEY_WORDS_FILTER, "black");
            }
        }

        //检查审核词
        if (null != carrierCheckWordsPattern) {
            Matcher matcher = carrierCheckWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.put(Constant.CARRIER_KEY_WORDS_FILTER, "check");
            }
        }

        //用运营商白词，尝试洗白黑词
        if (null != carrierWhiteWordsPattern && "black".equals(filterResult.get(Constant.CARRIER_KEY_WORDS_FILTER))) {
            Matcher matcher = carrierWhiteWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.remove(Constant.CARRIER_KEY_WORDS_FILTER);
            }
        }

        logger.info("[Filters]:运营商关键词过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }

}
