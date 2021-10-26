package com.smoc.cloud.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 用户角色判定
 * <p>
 * Description:根据访问url所需要的角色，与客户角色匹配，来决定客户是否有访问权限
 * </p>
 */
@Slf4j
public class MpmAccessDecisionManager extends AbstractAccessDecisionManager {

    public MpmAccessDecisionManager(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if (configAttributes == null) {
            return;
        }

        //返回默认的角色名称
        String tempRoleName = "tempAuthentication";
        Boolean status = false;

        //访问资源需要的角色
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String roleName = (ca).getAttribute();

            //如果返回的是默认角色
            if (tempRoleName.equals(roleName)) {
                status = true;
                break;
            }

            //两种角色进行校验
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (roleName.equals(ga.getAuthority())) {
                    return;
                }
            }

        }

        //如果返回的是默认角色
        if (status) {
            log.warn("[系统安全][安全拦截]:非法访问资源:访问超出了系统范围！");
            throw new AccessDeniedException("BeyondSystemMenusException");
        }

        //没有权限的访问
        //log.error("非法访问无权限资源:AccessDeniedException");
        throw new AccessDeniedException("AccessDeniedException");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    /**
     * Iterates through all <code>AccessDecisionVoter</code>s and ensures each can
     * support the presented class.
     * <p>
     * If one or more voters cannot support the presented class, <code>false</code>
     * is returned.
     *
     * @param clazz the type of secured object being presented
     * @return true if this type is supported
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
