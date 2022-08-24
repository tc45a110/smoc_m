export _HOME=/apps/test/business-proxy
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx2048m -Xms1024m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/alidata/test/business-proxy/logs -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.business.proxy.server.ProxyServer

