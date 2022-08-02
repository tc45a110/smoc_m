package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimStatusChangeRequest extends BaseRequest{

    private String iccid;

    //变更类型
    private String changeType;

}
