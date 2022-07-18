package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IotUserProductItemsValidator {

    private String id;

    private String userId;

    private String productId;

    private String createdBy;

    private Date createdTime;
}
