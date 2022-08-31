#!/bin/sh
. /etc/profile
. ~/.bash_profile

#返回统计
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.finance.service.FinanceReturnStatisticsService >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
