#!/bin/sh
. /etc/profile
. ~/.bash_profile

#返回统计
cd /apps/business-statistics
java -classpath .:./conf:./lib/* com.business.statistics.finance.service.FinanceReturnStatisticsService >> /alidata/business-statistics/logs/`date "+%Y-%m-%d"`.log &
