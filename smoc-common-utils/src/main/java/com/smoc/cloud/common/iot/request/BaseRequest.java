package com.smoc.cloud.common.iot.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class BaseRequest {

    private String account;

    private String timestamp;
}
