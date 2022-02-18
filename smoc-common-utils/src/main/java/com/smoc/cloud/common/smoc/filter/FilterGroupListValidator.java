package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Setter
@Getter
public class FilterGroupListValidator {

    private String id;
    @NotNull(message = "编码不能为空！")
    private String enterpriseId;
    private String groupId;
    @NotNull(message = "群组名称不能为空！")
    @Size(min = 1, max = 20, message = "群组名称长度不符合规则！")
    private String groupName;
    private String groupCode;
    private String parentId;
    private String isLeaf;
    private String status;
    private int sort;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
