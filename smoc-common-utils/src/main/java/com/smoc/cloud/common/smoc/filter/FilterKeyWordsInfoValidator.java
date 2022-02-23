package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class FilterKeyWordsInfoValidator {
    private String id;

    @NotNull(message = "关键词业务类别不能为空！")
    private String keyWordsBusinessType;

    @NotNull(message = "业务ID不能为空！")
    private String businessId;

    @NotNull(message = "关键词类别不能为空！")
    private String keyWordsType;

    @NotNull(message = "关键词不能为空！")
    @Length(max = 128, message = "关键词最大长度为{max}个字符")
    private String keyWords;

    @Length(max = 255, message = "关键词描述最大长度为{max}个字符")
    private String keyDesc;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdTimeStr;

    List<FilterKeyWordsInfoValidator> filterKeyWordsList = new ArrayList<>();
    List<ExcelModel> exccelList = new ArrayList<>();
}
