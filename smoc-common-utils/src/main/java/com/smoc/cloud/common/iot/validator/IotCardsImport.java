package com.smoc.cloud.common.iot.validator;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IotCardsImport {

    @CsvBindByPosition(position = 0)
    private String msisdn;

    @CsvBindByPosition(position = 1)
    private String iccid;

    @CsvBindByPosition(position = 2)
    private String imsi;

    @CsvBindByPosition(position = 3)
    private String openDate;

    @CsvBindByPosition(position = 4)
    private String activeDate;

    @CsvBindByPosition(position = 5)
    private String networkType;

    @CsvBindByPosition(position = 6)
    private String province;

    @CsvBindByPosition(position = 7)
    private String cardStatus;


}
