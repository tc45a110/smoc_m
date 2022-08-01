package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PackageCardsPageRequest extends BaseRequest {

    private String packageId;

    //当前页
    private Integer currentPage;
}
