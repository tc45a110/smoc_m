#!/bin/sh
. /etc/profile
. ~/.bash_profile
sleep 10
#
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.access.MessageWebTaskInfo -1 >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
