package com.base.common.vo;

import java.io.Serializable;

public class DelayRateMessageStatistics  implements Serializable {
    private static final long serialVersionUID = 110000L;

    private int statisticsNumber;

    private int delayNumber;

    private double delayRate;

    private String statisticsTime;

    public int getStatisticsNumber() {
        return statisticsNumber;
    }

    public void setStatisticsNumber(int statisticsNumber) {
        this.statisticsNumber = statisticsNumber;
    }

    public int getDelayNumber() {
        return delayNumber;
    }

    public void setDelayNumber(int delayNumber) {
        this.delayNumber = delayNumber;
    }

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public double getDelayRate() {
        return delayRate;
    }

    public void setDelayRate(double delayRate) {
        this.delayRate = delayRate;
    }

    @Override
    public String toString() {
        return "DelayRateMessageStatistics{" +
                "statisticsNumber=" + statisticsNumber +
                ", delayNumber=" + delayNumber +
                ", delayRate=" + delayRate +
                ", statisticsTime='" + statisticsTime + '\'' +
                '}';
    }
}
