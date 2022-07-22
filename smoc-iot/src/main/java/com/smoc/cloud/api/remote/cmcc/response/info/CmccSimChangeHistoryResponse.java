package com.smoc.cloud.api.remote.cmcc.response.info;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CmccSimChangeHistoryResponse {

    private List<CmccSimChangeHistoryItemResponse> changeHistoryList;
}
