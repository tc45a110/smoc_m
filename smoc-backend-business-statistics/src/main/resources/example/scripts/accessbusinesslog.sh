#!/bin/sh
. /etc/profile
. ~/.bash_profile
sleep 10
#
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.access.AccessBusinessMtLog -1 >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
sleep 10
java -classpath .:./config:./lib/* com.business.statistics.message.access.AccessBusinessMrLog -1 >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
