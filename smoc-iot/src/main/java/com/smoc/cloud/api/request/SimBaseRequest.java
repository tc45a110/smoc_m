package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimBaseRequest extends BaseRequest{

    private String msisdn;

    private String iccid;

    private String imsi;
}
