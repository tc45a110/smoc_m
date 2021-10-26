package com.smoc.cloud.auth.params.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.params.entity.BaseSystemParamConfiguation;
import com.smoc.cloud.auth.params.repository.BaseSystemParamConfiguationRepository;
import com.smoc.cloud.common.auth.qo.ConfiguationParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 2020/5/30 14:37
 **/
@Slf4j
@Service
public class BaseSystemParamConfiguationService {

    @Resource
    private BaseSystemParamConfiguationRepository baseSystemParamConfiguationRepository;


    public ResponseData<List<ConfiguationParams>> list(ConfiguationParams param){

        List<ConfiguationParams> list = baseSystemParamConfiguationRepository.findConfiguationParams(param);
        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(list);
    }


    //分页查询
    public PageList<ConfiguationParams> page(PageParams<ConfiguationParams> pageParams) {
        return baseSystemParamConfiguationRepository.page(pageParams);
    }

    /**
     * 根据ID 查询角色
     *
     * @return
     */
    public ResponseData<BaseSystemParamConfiguation> findById(String id) {

        Optional<BaseSystemParamConfiguation> data = baseSystemParamConfiguationRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    @Transactional
    public ResponseData save(List<BaseSystemParamConfiguation> paramsList, String op) {


        ConfiguationParams param = new ConfiguationParams();
        param.setUserId(op);
        //查询数据
        PageParams<ConfiguationParams> params = new PageParams<>();
        params.setPageSize(200);
        params.setCurrentPage(1);
        params.setParams(param);
        PageList<ConfiguationParams> page = this.page(params);

        //先删除以前保存的参数
        log.info("[系统参数管理][保存参数前删除以前数据]数据:{}", JSON.toJSONString(page.getList()));
        baseSystemParamConfiguationRepository.deleteAllByUserId(op);

        //记录日志
        log.info("[系统参数管理][批量保存数据]数据:{}", JSON.toJSONString(paramsList));
        baseSystemParamConfiguationRepository.saveAll(paramsList);
        return ResponseDataUtil.buildSuccess();
    }


    @Transactional
    public ResponseData<BaseSystemParamConfiguation> deleteById(String id) {
        //记录日志
        BaseSystemParamConfiguation data = baseSystemParamConfiguationRepository.findById(id).get();
        log.info("[系统参数管理][delete]数据:{}", JSON.toJSONString(data));

        baseSystemParamConfiguationRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

}
