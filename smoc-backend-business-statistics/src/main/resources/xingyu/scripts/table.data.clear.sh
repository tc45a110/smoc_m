#!/bin/sh
. /etc/profile
. ~/.bash_profile

cd /smoc/apps/business-statistics
java -classpath .:./config:./lib/* com.business.statistics.table.TableDataClear >> /smoc/logs/business-statistics/`date "+%Y-%m-%d"`.log &
