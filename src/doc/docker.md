Docker Camunda
````
sudo docker image build --rm -t mikhailbolshakov/camunda-tomcat ./ 

sudo docker container run --rm -it --network=host mikhailbolshakov/camunda-tomcat

# remove all untagged images
docker rmi $(docker images | grep "^<none>" | awk "{print $3}")

````

Docker mysql
````
sudo docker run -p 3306:3306 --name=mysql -e MYSQL_ROOT_PASSWORD=root -d mysql/mysql-server
mysql -h localhost -P 3306 --protocol=tcp -u root
````