package com.smoc.cloud.service;

import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.utils.FilterChainInitialize;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 消息发送验证服务
 */
public class FilterService {

    public static Logger logger = Logger.getLogger(FilterChain.class.toString());

    //过滤基础数据接口
    private LoadDataService loadDataService;

    public FilterService(LoadDataService loadDataService) {
        this.loadDataService = loadDataService;
    }

    /**
     * 消息发送过滤验证
     * 描述：完成对消息内容进行系统关键字、账号关键字、通道关键字、运营商关键字、信息分类关键字进行黑词、审核词、白词进行校验；
     * 对省份是否屏蔽、业务账号发送量限制、单个号码该通道发送量限制等进行判定操作；
     *
     * @param params 短消息参数
     * @return Map<String, String>  返回map.size<0表示通过验证，map.size>0时候，value为每个过滤器约定的含义来进行业务逻辑处理
     */
    public Map<String, String> validateMessage(ParamModel params) {

        //filterResult 存放执行完结果，如果filterResult size=0，则执行通过,如果 size>0,可以根据对应返回值，处理对应业务逻辑
        Map<String, String> filterResult = new HashMap<>();
        FilterChain filterChain = FilterChainInitialize.filterChain;
        filterChain.index =0;
        filterChain.doFilter(params, loadDataService, filterResult, filterChain);
        return filterResult;

    }
}
