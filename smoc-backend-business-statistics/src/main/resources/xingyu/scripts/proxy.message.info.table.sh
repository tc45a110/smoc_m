#!/bin/sh
. /etc/profile
. ~/.bash_profile

#proxy_message_info分天表的维护
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.table.ProxyMessageInfoTable  >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
