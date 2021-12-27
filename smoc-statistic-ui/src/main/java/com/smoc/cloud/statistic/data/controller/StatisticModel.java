package com.smoc.cloud.statistic.data.controller;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatisticModel {

    String[] date;

    Integer[] sendAmount;

    Integer[] incomeAmount;

    Integer[] costAmount;

    Integer[] profitAmount;
}
