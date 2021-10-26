package com.smoc.cloud.admin.utils;

import com.smoc.cloud.common.auth.entity.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 安全过滤白名单
 */
public class SecurityMenus {

    public static List<Menu> getSecurityMenus() {

        List<Menu> menus = new ArrayList<>();

        Menu loginMenu = new Menu();
        loginMenu.setModulePath("/**/login");
        menus.add(loginMenu);

        Menu loginMenu1 = new Menu();
        loginMenu1.setModulePath("/**/token/login/*");
        menus.add(loginMenu1);

        Menu ssoMenu = new Menu();
        ssoMenu.setModulePath("/**/token/sso/*");
        menus.add(ssoMenu);

        Menu logoutMenu = new Menu();
        logoutMenu.setModulePath("/**/logout");
        menus.add(logoutMenu);

        Menu securityMenu = new Menu();
        securityMenu.setModulePath("/**/security_login");
        menus.add(securityMenu);

        Menu pv = new Menu();
        pv.setModulePath("/**/counter/pv/*");
        menus.add(pv);

        Menu uv = new Menu();
        uv.setModulePath("/**/counter/uv/*/*");
        menus.add(uv);

        Menu parise = new Menu();
        parise.setModulePath("/**/counter/parise/*");
        menus.add(parise);

        Menu channel = new Menu();
        channel.setModulePath("/**/channel/find");
        menus.add(channel);

        Menu news = new Menu();
        news.setModulePath("/**/news/view/*");
        menus.add(news);

        Menu token = new Menu();
        token.setModulePath("/**/weixin/token");
        menus.add(token);

        return menus;
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
