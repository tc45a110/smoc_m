package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class IntellectShortUrlValidator {

    private String id;

    private String tplId;

    private String custFlag;

    private String custId;

    private String dyncParams;

    private String customUrl;

    private String extData;

    private String aimUrl;

    private String aimCode;

    //显示次数 群发单个智能短信短链短信给 N 个号码 的场景，防止展示次数超过预期带来安 全风险，指定此智能短信短链最大成功 展示次数。
    private Integer showTimes;

    //失效时间 智能短信编码类型为群发：时间可任意 填，不填则默认 7 天； 智能短信编码类型为个性化：时间最小 1 天，最大 7
    private Integer expireTimes;

    private BigDecimal currentPrice;

    private BigDecimal costPrice;

    private String factories;

    private String resultCode;

    private String errorMessage;

    private String isGiveBack;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String enterpriseName;

    private String tplName;

    //短链的有效状态
    private String status;
}
