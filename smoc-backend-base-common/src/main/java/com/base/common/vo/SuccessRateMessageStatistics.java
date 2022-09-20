package com.base.common.vo;

import java.io.Serializable;
import java.util.Date;

public class SuccessRateMessageStatistics implements Serializable {
    private static final long serialVersionUID = 100001L;

    private int statisticsSendNumber;
    private int statisticsReportNumber;
    private int successNumber;
    private int failNumber;
    private double successRate;
    private String statisticsTime;

    public int getStatisticsSendNumber() {
        return statisticsSendNumber;
    }

    public void setStatisticsSendNumber(int statisticsSendNumber) {
        this.statisticsSendNumber = statisticsSendNumber;
    }

    public int getStatisticsReportNumber() {
        return statisticsReportNumber;
    }

    public void setStatisticsReportNumber(int statisticsReportNumber) {
        this.statisticsReportNumber = statisticsReportNumber;
    }

    public int getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(int successNumber) {
        this.successNumber = successNumber;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    @Override
    public String toString() {
        return "SuccessRateMessageStatistics{" +
                "statisticsSendNumber=" + statisticsSendNumber +
                ", statisticsReportNumber=" + statisticsReportNumber +
                ", successNumber=" + successNumber +
                ", failNumber=" + failNumber +
                ", successRate=" + successRate +
                ", statisticsTime='" + statisticsTime + '\'' +
                '}';
    }
}
