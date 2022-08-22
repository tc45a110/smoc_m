export _HOME=/smoc/apps/protocol-proxy-smpp
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx1024m -Xms1024m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/protocol-proxy-smpp -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.protocol.proxy.server.ProxyServer
 
