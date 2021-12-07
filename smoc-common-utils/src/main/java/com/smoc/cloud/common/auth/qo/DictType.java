package com.smoc.cloud.common.auth.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典通用类
 * 2019/5/23 15:49
 **/
@Getter
@Setter
public class DictType {

    private String icon;

    private List<Dict> dict = new ArrayList<>();

}
