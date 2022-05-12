package com.smoc.cloud.intelligence.remote.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseParamArr {

    private Integer type;

    private String name;

    private Integer hasLength;

    private Integer lengthRestrict;

    private Integer minLength;

    private Integer maxLength;

    private Integer fixLength;
}
