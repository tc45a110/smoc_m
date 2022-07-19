package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IotFlowCardsInfo {

    private IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator;

    private IotFlowCardsSecondaryInfoValidator iotFlowCardsSecondaryInfoValidator;
}
