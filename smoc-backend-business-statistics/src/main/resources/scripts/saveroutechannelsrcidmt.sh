#!/bin/sh
. /etc/profile
. ~/.bash_profile

#每分钟记录前一分钟的下发记录，根据channelsrcid进行分类分表入库
cd /apps/business-statistics
java -classpath .:./conf:./lib/* com.business.statistics.message.proxy.ProxyBusinessMtLog -1 >> /alidata/business-statistics/logs/`date "+%Y-%m-%d"`.log &
