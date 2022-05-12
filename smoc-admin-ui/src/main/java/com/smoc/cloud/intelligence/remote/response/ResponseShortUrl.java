package com.smoc.cloud.intelligence.remote.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResponseShortUrl {

    //模板ID 智能短信平台生成的模板 ID，由 9 位数字组成。
    private String tplId;

    private List<ResponseParamList> paramList;
}
