package com.smoc.cloud.common.auth.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bootstrap-treeview json 结构
 * 2019/4/23 14:39
 **/
@Setter
@Getter
public class RoleNodes {

    private  String id;

    private String text;

    private String icon;

    private String systemId;

    private Integer isOperating;

    private Map<String, Object> state;

    private boolean lazyLoad;

    private List<RoleNodes> nodes = new ArrayList<RoleNodes>();
}
