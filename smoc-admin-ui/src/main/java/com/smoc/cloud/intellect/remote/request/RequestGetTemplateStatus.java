package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取模板状态
 */
@Setter
@Getter
public class RequestGetTemplateStatus {

    private List<String> tplIds;
}
