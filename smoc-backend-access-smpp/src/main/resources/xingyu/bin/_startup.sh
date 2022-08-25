export _HOME=/smoc/apps/protocol-access-smpp
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx2048m -Xms2048m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/protocol-access-smpp -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.protocol.access.smpp.MinaSmpp