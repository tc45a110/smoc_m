#!/bin/sh
. /etc/profile
. ~/.bash_profile

#
cd /apps/business-statistics
java -classpath .:./conf:./lib/* com.business.statistics.message.access.AccessBusinessMtLog -1 >> /alidata/business-statistics/logs/`date "+%Y-%m-%d"`.log &
