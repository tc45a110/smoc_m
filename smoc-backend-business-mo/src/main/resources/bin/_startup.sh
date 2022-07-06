export _HOME=/apps/smoc-business-mo
export _LIB=$_HOME/lib
export _CONFIG=$_HOME/config

#Change current path
cd $_HOME
echo `pwd`

java -classpath $_CONFIG:$_LIB/* -server -Xmx512m -Xms512m -Dfile.encoding=UTF-8 com.business.mo.server.MOServer
 
