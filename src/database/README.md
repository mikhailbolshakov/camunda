````
docker build --rm -t camunda-mysql -f ./src/database/Dockerfile ./src/database

docker run -p 3306:3306 --name=camunda-mysql -e MYSQL_ROOT_PASSWORD=root -d camunda-mysql 

mysql -h localhost -P 3306 --protocol=tcp -u root
````