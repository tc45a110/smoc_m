package com.smoc.cloud.api.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimStopReasonResponse {

    private String platformType;

    private String stopReason;

    private String shutdownReasonDesc;
}
