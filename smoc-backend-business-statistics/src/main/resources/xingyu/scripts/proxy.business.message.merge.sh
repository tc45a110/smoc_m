#!/bin/sh
. /etc/profile
. ~/.bash_profile

#代理业务层 mt下发记录和mr回执记录 以客户提交时间(取天yyyyMMdd)为后缀  proxy_message_info_yyyyMMdd
cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.message.proxy.ProxyBusinessMessageLogMerge 'afterMinute=-2' >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
