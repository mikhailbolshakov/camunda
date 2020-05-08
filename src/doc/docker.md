Docker 
````
docker build --rm -t mikhailbolshakov/camunda-tomcat ./ 

docker run --rm -it --network=host mikhailbolshakov/camunda-tomcat

docker run --rm -it --network=host --env-file ./env.list mikhailbolshakov/camunda-tomcat

# remove all untagged images
docker rmi $(docker images | grep "^<none>" | awk "{print $3}")

````

Docker mysql
````
docker build --rm -t camunda-mysql -f ./src/database/Dockerfile ./

docker run -p 3306:3306 --name=mysql -e MYSQL_ROOT_PASSWORD=root -d mysql/mysql-server 


mysql -h localhost -P 3306 --protocol=tcp -u root
````

Environment variables
````
export CAMUNDA_LOG_DIR=$HOME/logs
export CAMUNDA_LOG_LEVEL=DEBUG
export CAMUNDA_DB_HOST=localhost
export CAMUNDA_DB_PORT=3306
export CAMUNDA_DB_USERNAME=root
export CAMUNDA_DB_PASSWORD=root

```` 