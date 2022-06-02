package com.smoc.cloud.common.smoc.index;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CheckRemindModel {

    private int signNum;

    private int templateNum;

    private int messageNum;

    private int totalNum;

}
