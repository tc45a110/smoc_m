package com.smoc.cloud.filter.white.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.filter.white.remote.WhiteFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;



/**
 * 白名单管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WhiteService {

    @Autowired
    private WhiteFeignClient whiteFeignClient;

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FilterWhiteListValidator>> page(PageParams<FilterWhiteListValidator> pageParams) {
        try {
            PageList<FilterWhiteListValidator> pageList = this.whiteFeignClient.page(pageParams);
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
    public ResponseData<FilterWhiteListValidator> findById(String id) {
        try {
            ResponseData data = this.whiteFeignClient.findById(id);
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
    public ResponseData save(FilterWhiteListValidator filterWhiteListValidator, String op) {
        try {
            ResponseData data = this.whiteFeignClient.save(filterWhiteListValidator, op);
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
            ResponseData data = this.whiteFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }



    /**
     * 批量保存
     * @param meipFileData
     * @param op
     * @return
     */
    /*public ResponseData bathSave(MeipFileData meipFileData, String op) {
        try {
            this.whiteFeignClient.bathSave(meipFileData, op);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
*/
}
