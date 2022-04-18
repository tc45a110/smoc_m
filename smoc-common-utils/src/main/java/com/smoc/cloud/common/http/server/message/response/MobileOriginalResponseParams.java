package com.smoc.cloud.common.http.server.message.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MobileOriginalResponseParams {

    private String account;

    private String mobile;

    //扩展号码
    private String extNumber;

    private String content;

    private String acceptTime;


}
