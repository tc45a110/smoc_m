package com.smoc.cloud.api.remote.cmcc.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimStopReasonResponse {

    private String platformType;

    private String stopReason;

    private String shutdownReasonDesc;
}
