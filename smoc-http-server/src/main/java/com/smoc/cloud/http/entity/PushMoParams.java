package com.smoc.cloud.http.entity;

import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PushMoParams {

    private String orderNo;

    private List<MobileOriginalResponseParams> data;

    private String timestamp;
}
