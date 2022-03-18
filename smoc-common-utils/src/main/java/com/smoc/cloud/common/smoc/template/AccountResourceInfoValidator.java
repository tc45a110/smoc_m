package com.smoc.cloud.common.smoc.template;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class AccountResourceInfoValidator {
    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;
    private String enterpriseId;
    private String resourceType;
    private String businessType;

    @NotNull(message = "资源备注不能为空！")
    @Size(min = 1, max = 10, message = "资源备注长度不符合规则！")
    private String resourceTitle;
    private String resourceAttchment;
    private Integer resourceAttchmentSize;
    private String resourceAttchmentType;
    private Integer resourceHeight;
    private Integer resourceWidth;
    private String resourceStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String fileType;

}
