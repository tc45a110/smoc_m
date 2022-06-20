#!/bin/sh
. /etc/profile
. ~/.bash_profile

#消费统计
cd /apps/business-statistics
java -classpath .:./conf:./lib/* com.business.statistics.finance.service.FinanceConsumeStatisticsService >> /alidata/business-statistics/logs/`date "+%Y-%m-%d"`.log &
