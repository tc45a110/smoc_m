package com.smoc.cloud.common.smoc.message;

import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class MessageComplaintInfoValidator {
    private String id;
    private String businessAccount;
    private String businessType;
    private String reportNumber;
    private String numberCode;
    private String channelId;
    private String carrierSource;

    @NotNull(message = "运营商不能为空！")
    private String carrier;

    private String sendDate;
    private String sendRate;
    private String reportContent;
    private String contentType;
    private String is12321;
    private String reportSource;
    private String reportDate;
    private String reportedNumber;
    private String reportedProvince;
    private String reportedCity;
    private String reportProvince;
    private String reportCity;
    private String reportUnit;
    private String reportChann;
    private String handleFlag;
    private String handleCarrierId;
    private String sendType;
    private String handleStatus;
    private String handleResult;
    private String handleRemark;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String startDate;
    private String endDate;
    private String complaintFiles;

    List<ComplaintExcelModel> complaintList = new ArrayList();
}
