package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.auth.data.provider.entity.OauthClientDetails;
import com.smoc.cloud.auth.data.provider.repository.OauthClientDetailsRepository;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author 武基慧
 * @Description //完成资源模块、客户端模块，优化菜单显示及资源服务器鉴权：判断资源对应的系统、资源是否有效，系统登出后增加回调地址到客户端
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthClientDetailsService {

    @Resource
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    /**
     * 查询数据列表
     *
     * @return
     */
    public ResponseData<Iterable<OauthClientDetails>> findAll() {
        Iterable<OauthClientDetails> data = oauthClientDetailsRepository.findAll();
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据ID 查询
     *
     * @return
     */
    public ResponseData<OauthClientDetails> findById(String id) {

        Optional<OauthClientDetails> data = oauthClientDetailsRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @Transactional
    public ResponseData save(OauthClientDetails entity, String op) {

        if (!StringUtils.isEmpty(entity.getClientSecret())) {
            entity.setClientSecret(MpmEncryptPasswordEncoder.getPasswordEncoder().encode(entity.getClientSecret().trim()));
        }

        //add查重roleCode、roleName
        if ("add".equals(op) && this.exists(entity.getClientId())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重roleCode、roleName
        else if ("edit".equals(op) && StringUtils.isEmpty(entity.getClientSecret())) {
            OauthClientDetails data = oauthClientDetailsRepository.findById(entity.getClientId()).get();
            entity.setClientSecret(data.getClientSecret());
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[客户端管理][{}]数据:{}",op,JSON.toJSONString(entity));
        oauthClientDetailsRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 修改客户端密码
     *
     * @param id
     * @param secret
     */
    @Transactional
    public ResponseData resetClientSecret(String id, String secret) {

        secret = MpmEncryptPasswordEncoder.getPasswordEncoder().encode(secret.trim());

        OauthClientDetails data = oauthClientDetailsRepository.findById(id).get();
        //记录日志
        log.info("[客户端管理][重置密码]数据:{}",JSON.toJSONString(data));
        oauthClientDetailsRepository.resetClientSecret(id, secret);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<OauthClientDetails> deleteById(String id) {

        //记录日志
        log.info("[客户端管理][delete]数据:{}",id);
        oauthClientDetailsRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    public boolean exists(String id) {

        return oauthClientDetailsRepository.existsById(id);
    }
}
