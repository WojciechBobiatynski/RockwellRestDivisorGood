TOMCAT_PATH=/usr/local/tomcat
CATALINA_PID=$TOMCAT_PATH/temp/tomcat.pid
CATALINA_HOME=$TOMCAT_PATH
CATALINA_BASE=$TOMCAT_PATH
export JAVA_OPTS="$JAVA_OPTS \
-Xmx1024m \
-Dgryf_db_user=$GRYF_DB_USER \
-Dgryf_db_pass=$GRYF_DB_PASS \
-Dgryf_db_sid=$GRYF_DB_SID \
-Dgryf_db_port=$GRYF_DB_PORT \
-Dgryf_db_ip=$GRYF_DB_IP \
-Dlogback.configurationFile=$TOMCAT_PATH/conf/logback.xml \
-DdevMode=true"

APP_CONFIG_DIR=$TOMCAT_PATH/conf
CLASSPATH=$APP_CONFIG_DIR
CATALINA_OPTS="$CATALINA_OPTS -server -XX:+UseParallelGC -Xms512M -Xmx1024m -XX:MaxMetaspaceSize=512m"
#GC
#CATALINA_OPTS=%CATALINA_OPTS% -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled
#CATALINA_OPTS=%CATALINA_OPTS% -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70
#CATALINA_OPTS=%CATALINA_OPTS% -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
CATALINA_OPTS="$CATALINA_OPTS -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:$CATALINA_HOME/logs/gc/gc.log"
CATALINA_OPTS="$CATALINA_OPTS -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
CATALINA_OPTS="$CATALINA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$CATALINA_HOME/heapdump/"

export CATALINA_OPTS

export CLASSPATH