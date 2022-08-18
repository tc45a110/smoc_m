package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExportRegisterModel {

    private String carrier;

    private String registerOrderNo;

    private List<String> ids;

    private String createdBy;
}
