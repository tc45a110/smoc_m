export _HOME=/smoc/apps/business-access
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx8192m -Xms8192m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/business-access -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.business.access.server.AccessServer 
 
