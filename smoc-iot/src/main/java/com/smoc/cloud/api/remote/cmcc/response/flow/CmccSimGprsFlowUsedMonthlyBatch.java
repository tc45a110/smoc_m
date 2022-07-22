package com.smoc.cloud.api.remote.cmcc.response.flow;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CmccSimGprsFlowUsedMonthlyBatch {

    private List<CmccSimGprsFlowUsedMonthlyBatchItem> dataAmountList;
}
