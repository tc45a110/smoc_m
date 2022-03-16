package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class MessageAccountValidator {

    private String accountId;

    private String enterpriseId;

    private String businessType;

    private String infoType;

    private BigDecimal accountUsableSum;

    private String startDate;

    private String endDate;
}
