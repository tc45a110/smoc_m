package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class IotUserProductInfoValidator {

    private String id;

    private String userId;

    private String createdBy;

    private String createdTime;

    private List<IotProductInfoValidator> productList = new ArrayList<>();
}
