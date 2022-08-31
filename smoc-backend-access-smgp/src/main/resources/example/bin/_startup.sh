export _HOME=/smoc/apps/protocol-access-smgp
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx512m -Xms512m -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/smoc/logs/protocol-access-smgp -Ddruid.mysql.usePingMethod=false -Dfile.encoding=UTF-8 com.protocol.access.smgp.MinaSmgp
