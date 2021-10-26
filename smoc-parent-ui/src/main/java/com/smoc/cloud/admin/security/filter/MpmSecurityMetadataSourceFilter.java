package com.smoc.cloud.admin.security.filter;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.utils.SecurityMenus;
import com.smoc.cloud.common.auth.entity.Menu;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 初始化系统权限管理角色及功能
 * <p>
 * Description:系统启动时初始化，获取到所有资源及其对应角色。 核心的地方，每个请求，进行拦截，判定资源对应的角色权限的认定。
 * </p>
 */
@Slf4j
public class MpmSecurityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {


    private OauthTokenService oauthTokenService;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    //系统要保护的全局权限
    private static Map<String, Collection<ConfigAttribute>> aclResourceMap = null;

    /**
     * 构造方法 这进行 动态注入不了，用构造器来实现
     */
    public MpmSecurityMetadataSourceFilter(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;

        //加载全局权限关系
        loadResourceDefine();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 访问控制，每个访问将在这来过滤，并判断是否请求在权限保护之内，是否要进行权限验证 平台权限访问
     * 才用白名单原则，分为有角色的url控制和无角色的url控制，
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        /**
         * 请求的url
         */
        String requestURI = request.getRequestURI();
        //log.info("请求uri：{}", requestURI);

        //跟路径，则返回
        String contextPath = request.getContextPath();
        if(requestURI.equals(contextPath) || requestURI.equals(contextPath+"/")){
            return null;
        }

        /**
         * auth获取的受保护的URI
         */
        Iterator<String> ite = aclResourceMap.keySet().iterator();
        Collection<ConfigAttribute> atts = null;

        while (ite.hasNext()) {
            //路径匹配模式
            String patternPrefix = "/**/";
            String patternPath = ite.next();
            if (pathMatcher.match(patternPrefix + patternPath, requestURI)) {
                atts = aclResourceMap.get(patternPath);
                break;
            }
        }

        //如果有权限异常，返回
        if (null != atts) {
            return atts;
        }

        //过滤白名单
        List<Menu> menus = SecurityMenus.getSecurityMenus();
        for (Menu menu : menus) {
            if (pathMatcher.match(menu.getModulePath(), requestURI)) {
                return null;
            }
        }

        //如果没有角色，同样也没在白名单里 返回一个默认的角色 并在 MpmAccessDecisionManager 里处理
        Collection<ConfigAttribute> tempConfigAttribute = new ArrayList<>();
        ConfigAttribute ca = new SecurityConfig("tempAuthentication");
        tempConfigAttribute.add(ca);
        log.warn("[系统安全][安全拦截]:非法请求URI：{}",requestURI);

        return tempConfigAttribute;
    }

    /**
     * 初始化系统要保护的系统权限，在此处不加载的权限将得不到保护 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
     * 加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行。
     */
    public void loadResourceDefine() {

        log.info("[系统启动][系统安全]数据:加载全局系统保护角色及资源");
        //加载系统角色、资源信息
        RoleMenus[] roleMenus = oauthTokenService.loadRoleMenus();

        aclResourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        if (null == roleMenus) {
            return;
        }

        //组织 ConfigAttribute
        for (RoleMenus rm : roleMenus) {
            ConfigAttribute ca = new SecurityConfig(rm.getRoleCode());
            String path = rm.getMenusPath();
            if (!StringUtils.isEmpty(path)) {
                if (aclResourceMap.containsKey(path)) {
                    Collection<ConfigAttribute> atts = aclResourceMap.get(path);
                    atts.add(ca);
                    aclResourceMap.put(path, atts);
                } else {
                    Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
                    atts.add(ca);
                    aclResourceMap.put(path, atts);
                }
            }
        }

    }

    @Override
    public boolean supports(Class<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
