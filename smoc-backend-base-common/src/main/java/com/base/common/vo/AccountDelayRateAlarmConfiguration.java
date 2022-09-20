package com.base.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountDelayRateAlarmConfiguration implements Serializable {

    private static final long serialVersionUID = 100002L;

    private String accountID;

    private int delayTimeThreshold;

    private double delayRateThreshold;

    private int statisticsNumber;

    private int evaluateNumber;

    private int evaluateIntervalTime;

    private List<DelayRateMessageStatistics> statisticsList;

    private int alreadyEvaluateNumber;
    private Date lastEvaluateDate;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getDelayTimeThreshold() {
        return delayTimeThreshold;
    }

    public void setDelayTimeThreshold(int delayTimeThreshold) {
        this.delayTimeThreshold = delayTimeThreshold;
    }

    public double getDelayRateThreshold() {
        return delayRateThreshold;
    }

    public void setDelayRateThreshold(double delayRateThreshold) {
        this.delayRateThreshold = delayRateThreshold;
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

    public List<DelayRateMessageStatistics> getStatisticsList() {
        return statisticsList;
    }

    public void setStatisticsList(List<DelayRateMessageStatistics> statisticsList) {
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

    public int getStatisticsNumber() {
        return statisticsNumber;
    }

    public void setStatisticsNumber(int statisticsNumber) {
        this.statisticsNumber = statisticsNumber;
    }

    @Override
    public String toString() {
        return "AccountDelayRateAlarmConfiguration{" +
                "accountID='" + accountID + '\'' +
                ", delayTimeThreshold=" + delayTimeThreshold +
                ", delayRateThreshold=" + delayRateThreshold +
                ", statisticsNumber=" + statisticsNumber +
                ", evaluateNumber=" + evaluateNumber +
                ", evaluateIntervalTime=" + evaluateIntervalTime +
                ", statisticsList=" + statisticsList +
                ", alreadyEvaluateNumber=" + alreadyEvaluateNumber +
                ", lastEvaluateDate=" + lastEvaluateDate +
                '}';
    }
}
