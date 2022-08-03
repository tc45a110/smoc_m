package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class PackageCardsPageRequest extends BaseRequest {

    @NotNull(message = "套餐包ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{0,32}", message = "套餐包ID不符合规则不符合规则！")
    private String packageId;

    //当前页
    @Pattern(regexp = "^\\d+$", message = "当前页不符合规则！")
    private String currentPage;
}
