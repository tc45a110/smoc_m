package com.smoc.cloud.common.auth.qo;

import lombok.Getter;
import lombok.Setter;

/**
 * 列表查询对象
 * 2019/5/6 13:40
 **/
@Setter
@Getter
public class Users {

    private String id;

    private String userName;

    private String phone;

    private Integer active;

    private String realName;

    private String organization;

    private String corporation;

    private String code;

    private Integer type;
}
