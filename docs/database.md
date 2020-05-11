## local MySQL

* Create a database with `camunda` name
* Apply all the scripts from the folder `src/database/setup`
* Access to `camunda` database by `camunda\camunda` credentials

## docker image MySQL

Run the following docker commands from root project directory

````
docker build --rm -t camunda-mysql -f ./src/database/Dockerfile ./src/database

docker run -p 3306:3306 --name=camunda-mysql -e MYSQL_ROOT_PASSWORD=root -d camunda-mysql 

mysql -h localhost -P 3306 --protocol=tcp -u camunda --password=camunda
````

## environment variables

Be aware that if docker has network=bridge (default) mode, you cannot access from within container host by localhost

````
CAMUNDA_DB_HOST=localhost 
CAMUNDA_DB_PORT=3306
CAMUNDA_DB_USERNAME=camunda
CAMUNDA_DB_PASSWORD=camunda
````