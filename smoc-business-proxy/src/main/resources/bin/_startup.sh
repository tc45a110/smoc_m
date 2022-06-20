export _HOME=/apps/smoc-business-proxy
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx1024m -Xms1024m -Dsun.net.client.defaultConnectTimeout=60000 -Dsun.net.client.defaultReadTimeout=180000 -Dfile.encoding=UTF-8 com.business.proxy.server.ProxyServer