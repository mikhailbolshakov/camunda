## MySQL

* Create a database with `camunda` name
* Apply all the scripts from the folder `src/database/setup`
* Access to `camunda` database by `camunda\camunda` credentials

#### MySQL with docker

Run the following docker commands from the root project directory

````
docker build --rm -t camunda-mysql -f ./src/database/Dockerfile ./src/database

docker run -p 3306:3306 --name=camunda-mysql -e MYSQL_ROOT_PASSWORD=root -d camunda-mysql 

mysql -h localhost -P 3306 --protocol=tcp -u camunda --password=camunda
````
Be aware that if docker has network=bridge (default) mode, you cannot access MySQL container by localhost
You can either use network=host or configure a docker network 

#### Environment variables

````
CAMUNDA_DB_HOST=localhost 
CAMUNDA_DB_PORT=3306
CAMUNDA_DB_USERNAME=camunda
CAMUNDA_DB_PASSWORD=camunda
````

## H2

For test purposes you can make use of H2 in-memory database
Note all data are clean up each time the server starts

#### Configuration

Setup the following configuration parameters in `application.properties`

````
spring.datasource.url=jdbc:h2:mem:camunda
spring.datasource.username=sa
spring.datasource.password=sa
spring.datasource.driverClassName=org.h2.Driver
````
 