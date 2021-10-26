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
public class Nodes {

    private String id;

    private String text;

    private String href;

    private String icon;

    private String system;

    private String orgCode;

    private String svcType;

    private boolean lazyLoad = true;

    private Map<String, Object> state;

    private List<Nodes> nodes = new ArrayList<Nodes>();
}
