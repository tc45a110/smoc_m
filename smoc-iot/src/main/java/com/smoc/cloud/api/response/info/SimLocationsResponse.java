package com.smoc.cloud.api.response.info;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimLocationsResponse {

    private String iccid;

    private String longitude;

    private String latitude;
}
