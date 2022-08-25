package com.smoc.cloud.api.remote.cmcc.response.info;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
public class CarrierInfo {

    private String carrierIdentifying;

    private String carrierPassword;
}
