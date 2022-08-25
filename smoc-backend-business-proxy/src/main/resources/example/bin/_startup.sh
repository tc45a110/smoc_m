export _HOME=/smoc/apps/business-proxy
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`
java -classpath $_CONFIG:$_LIB/* -server -Xmx2048m -Xms2048m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/business-proxy -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.business.proxy.server.ProxyServer
 
