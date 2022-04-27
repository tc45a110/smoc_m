package com.smoc.cloud.main;

import com.google.gson.Gson;
import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MainUtils {

    @Autowired
    private OauthTokenService oauthTokenService;

    @Async
    public void setReidsData(ResponseData<Nodes[]> businessTypes, ValueOperations<String,String> vo, SecurityUser user) {
        Gson gson = new Gson();
        for(int i=1;i<businessTypes.getData().length;i++){
            String value = vo.get(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypes.getData()[i].getText());
            if(StringUtils.isEmpty(value)){
                ResponseData<Nodes[]> data = oauthTokenService.getAllSubMenusByParentId(businessTypes.getData()[i].getId());
                vo.set(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypes.getData()[i].getText(), gson.toJson(data.getData()));
            }
        }
    }
}
