package com.smoc.cloud.intelligence.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取模版状态
 */
@Setter
@Getter
public class RequestGetTemplateStatus {

    private List<String> tplIds;
}
