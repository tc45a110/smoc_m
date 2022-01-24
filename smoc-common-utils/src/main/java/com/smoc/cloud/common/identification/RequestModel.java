package com.smoc.cloud.common.identification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestModel {

    private String orderNo;
    private String name;
    private String cardNo;
    private String faceBase64;
}
