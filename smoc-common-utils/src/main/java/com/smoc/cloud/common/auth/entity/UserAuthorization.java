package com.smoc.cloud.common.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限信息
 * 2019/4/4 23:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorization implements Serializable {

    //用户基本信息
    private SecurityUser user;
    //角色信息
    private List<Role> roles;
    //菜单信息
    private List<Menu> menus;

    public UserAuthorization(SecurityUser user,List<Menu> menus) {
        this.user = user;
        this.menus = menus;
    }


}
