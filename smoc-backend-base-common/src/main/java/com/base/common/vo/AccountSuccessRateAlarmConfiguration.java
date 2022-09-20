package com.base.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountSuccessRateAlarmConfiguration implements Serializable {

    private static final long serialVersionUID = 100000L;

    private String accountID;
    private int statisticsSendNumberThreshold;
    private double successRateThreshold;
    private int evaluateNumber;
    private int evaluateIntervalTime;

    private List<SuccessRateMessageStatistics> statisticsList;
    private int alreadyEvaluateNumber;
    private Date lastEvaluateDate;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getStatisticsSendNumberThreshold() {
        return statisticsSendNumberThreshold;
    }

    public void setStatisticsSendNumberThreshold(int statisticsSendNumberThreshold) {
        this.statisticsSendNumberThreshold = statisticsSendNumberThreshold;
    }

    public double getSuccessRateThreshold() {
        return successRateThreshold;
    }

    public void setSuccessRateThreshold(double successRateThreshold) {
        this.successRateThreshold = successRateThreshold;
    }

    public int getEvaluateNumber() {
        return evaluateNumber;
    }

    public void setEvaluateNumber(int evaluateNumber) {
        this.evaluateNumber = evaluateNumber;
    }

    public int getEvaluateIntervalTime() {
        return evaluateIntervalTime;
    }

    public void setEvaluateIntervalTime(int evaluateIntervalTime) {
        this.evaluateIntervalTime = evaluateIntervalTime;
    }

    public List<SuccessRateMessageStatistics> getStatisticsList() {
        return statisticsList;
    }

    public void setStatisticsList(List<SuccessRateMessageStatistics> statisticsList) {
        this.statisticsList = statisticsList;
    }

    public int getAlreadyEvaluateNumber() {
        return alreadyEvaluateNumber;
    }

    public void setAlreadyEvaluateNumber(int alreadyEvaluateNumber) {
        this.alreadyEvaluateNumber = alreadyEvaluateNumber;
    }

    public Date getLastEvaluateDate() {
        return lastEvaluateDate;
    }

    public void setLastEvaluateDate(Date lastEvaluateDate) {
        this.lastEvaluateDate = lastEvaluateDate;
    }

    @Override
    public String toString() {
        return "AccountSuccessRateAlarmConfiguration{" +
                "accountID='" + accountID + '\'' +
                ", statisticsSendNumberThreshold=" + statisticsSendNumberThreshold +
                ", successRateThreshold=" + successRateThreshold +
                ", evaluateNumber=" + evaluateNumber +
                ", evaluateIntervalTime=" + evaluateIntervalTime +
                ", statisticsList=" + statisticsList +
                ", alreadyEvaluateNumber=" + alreadyEvaluateNumber +
                ", lastEvaluateDate=" + lastEvaluateDate +
                '}';
    }
}
