#!/bin/sh
. /etc/profile
. ~/.bash_profile

#w未知返回统计
cd /apps/business-statistics
java -classpath .:./conf:./lib/* com.business.statistics.finance.service.UnknowStatisticsService >> /alidata/business-statistics/logs/`date "+%Y-%m-%d"`.log &
