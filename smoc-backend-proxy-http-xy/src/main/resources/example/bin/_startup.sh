export _HOME=/smoc/apps/protocol-proxy-http-xy
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx8192m -Xms8192m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/protocol-proxy-http-imms-telc -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.protocol.proxy.server.ProxyServer
