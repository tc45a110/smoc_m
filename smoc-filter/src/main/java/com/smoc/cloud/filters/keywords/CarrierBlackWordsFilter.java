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
 * 运营商黑词过滤
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class CarrierBlackWordsFilter implements Filter {

    public static Logger logger = Logger.getLogger(CarrierBlackWordsFilter.class.toString());

    private LoadDataService loadDataService;

    private String carrier;

    public CarrierBlackWordsFilter(LoadDataService loadDataService, String carrier) {
        this.loadDataService = loadDataService;
        this.carrier = carrier;
    }

    //业务账号黑词  Pattern
    private Pattern carrierBlackWordsPattern;


    /**
     * 运营商黑词过滤
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

        this.carrierBlackWordsPattern = loadDataService.getCarrierBlackWords(carrier);

        //检查黑词
        if (null != carrierBlackWordsPattern) {
            Matcher matcher = carrierBlackWordsPattern.matcher(message);
            if (matcher.find()) {
                filterResult.put(Constant.CARRIER_BLACK_WORDS_FILTER, "black");
            }
        }

        logger.info("[Filters]:运营商黑词过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }

}
