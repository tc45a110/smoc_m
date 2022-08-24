package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IotCardImportModel {

    private IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator;

    private List<IotCardsImport> cards;
}
