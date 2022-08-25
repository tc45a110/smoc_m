#!/bin/sh
. /etc/profile
. ~/.bash_profile
sleep 10
#每天根据前一天的业务日志，根据客户提交时间统计：客户提交数、提交成功数、提交失败数、状态报告成功数和状态报告失败数
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.proxy.ProxyBusinessMtLog  >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
