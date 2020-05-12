#### Logging configuration

Logging is based on `logback` library.

Loggers and appenders are configured in `./src/main/resources/logback.xml` file.

#### Logging to Elasticsearch

For ES integration `logstash` configured together with `logback`. Configuration file can be found here `./logstash/logstash.conf`.

According to configuration ES indices with template `logback-%{+YYYY.MM.dd}` are created.

Note, if logstash is running as a docker image a logstash input folder has to be linked (via docker volume) to a host folder configured in ${CAMUNDA_LOG_DIR} env





