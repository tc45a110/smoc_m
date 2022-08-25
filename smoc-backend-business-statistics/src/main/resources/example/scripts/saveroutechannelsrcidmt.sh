#!/bin/sh
. /etc/profile
. ~/.bash_profile
sleep 10
#每分钟记录前一分钟的下发记录，根据channelsrcid进行分类分表入库
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.proxy.ProxyBusinessMtLog -1 >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
