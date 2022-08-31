#!/bin/sh
. /etc/profile
. ~/.bash_profile

#消费统计
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.finance.service.FinanceConsumeStatisticsService >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
