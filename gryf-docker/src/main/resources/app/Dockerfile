FROM tomcat

COPY ./war/*.war /usr/local/tomcat/webapps/

COPY ./conf/setenv_origin.sh /usr/local/tomcat/bin/setenv.sh
COPY ./conf/logback_gryf.xml /usr/local/tomcat/conf/logback.xml
COPY ./conf/context.xml /usr/local/tomcat/conf/context.xml

COPY ./conf/*.properties /usr/local/tomcat/conf/

COPY ./lib/*.* /usr/local/tomcat/lib/

##COPY ./conf/configureNetwork.sh /usr/local/tomcat/conf/configureNetwork.sh

WORKDIR /usr/local/tomcat/conf

##RUN chmod +x configureNetwork.sh
