FROM tomcat:9.0

MAINTAINER mikhailbolshakov

COPY ./target/camundaDemo.war /usr/local/tomcat/webapps/