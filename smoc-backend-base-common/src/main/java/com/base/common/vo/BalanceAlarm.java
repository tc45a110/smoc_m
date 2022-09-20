package com.base.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BalanceAlarm implements Serializable {
    private static final long serialVersionUID = 2l;

    private String accountID;
    private Double alarmAmount;
    private Integer alarmFrequency;
    private Integer alarmNumber;

    private Double accountRechargeSum;

    private Integer alreadyAlarmNumber;
    private Date lastAlarmDate;

    public BalanceAlarm() {
    }

    public BalanceAlarm(String accountID, Double alarmAmount, Integer alarmFrequency, Integer alarmNumber) {
        this.accountID = accountID;
        this.alarmAmount = alarmAmount;
        this.alarmFrequency = alarmFrequency;
        this.alarmNumber = alarmNumber;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Double getAlarmAmount() {
        return alarmAmount;
    }

    public void setAlarmAmount(Double alarmAmount) {
        this.alarmAmount = alarmAmount;
    }

    public Integer getAlarmFrequency() {
        return alarmFrequency;
    }

    public void setAlarmFrequency(Integer alarmFrequency) {
        this.alarmFrequency = alarmFrequency;
    }

    public Integer getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(Integer alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public Integer getAlreadyAlarmNumber() {
        return alreadyAlarmNumber;
    }

    public void setAlreadyAlarmNumber(Integer alreadyAlarmNumber) {
        this.alreadyAlarmNumber = alreadyAlarmNumber;
    }

    public Date getLastAlarmDate() {
        return lastAlarmDate;
    }

    public void setLastAlarmDate(Date lastAlarmDate) {
        this.lastAlarmDate = lastAlarmDate;
    }

    public Double getAccountRechargeSum() {
        return accountRechargeSum;
    }

    public void setAccountRechargeSum(Double accountRechargeSum) {
        this.accountRechargeSum = accountRechargeSum;
    }

    @Override
    public String toString() {
        return "BalanceAlarm{" +
                "accountID='" + accountID + '\'' +
                ", alarmAmount=" + alarmAmount +
                ", alarmFrequency=" + alarmFrequency +
                ", alarmNumber=" + alarmNumber +
                ", accountRechargeSum=" + accountRechargeSum +
                ", alreadyAlarmNumber=" + alreadyAlarmNumber +
                ", lastAlarmDate=" + lastAlarmDate +
                '}';
    }
}