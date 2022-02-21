package com.smoc.cloud.filter.black.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.filter.black.remote.BlackFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 黑名单管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BlackService {

    @Autowired
    private BlackFeignClient blackFeignClient;

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FilterBlackListValidator>> page(PageParams<FilterBlackListValidator> pageParams) {
        try {
            PageList<FilterBlackListValidator> pageList = this.blackFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterBlackListValidator> findById(String id) {
        try {
            ResponseData data = this.blackFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(FilterBlackListValidator filterBlackListValidator, String op) {
        try {
            ResponseData data = this.blackFeignClient.save(filterBlackListValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除菜单数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.blackFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量保存
     * @param filterBlackListValidator
     * @return
     */
    public ResponseData batchSave(FilterBlackListValidator filterBlackListValidator) {
        try {
            this.blackFeignClient.bathSave(filterBlackListValidator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询要导出的数据
     * @param pageParams
     * @return
     */
    public ResponseData<List<ExcelModel>> excelModel(PageParams<FilterWhiteListValidator> pageParams) {
        try {
            ResponseData<List<ExcelModel>> list = this.blackFeignClient.excelModel(pageParams);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
