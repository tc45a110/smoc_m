package com.smoc.cloud.http.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageHttpsTaskInfo {

    private String id;
    private String templateId;
    private String businessAccount;
    private String expandNumber;
    private Integer splitNumber;
    private Integer submitNumber;
    private String messageContent;
    private String createdBy;
}
