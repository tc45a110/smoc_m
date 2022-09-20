export _HOME=/smoc/apps/protocol-proxy-http-imms-xzkj
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Dfile.encoding=UTF-8 com.protocol.proxy.server.AdminClient
 
