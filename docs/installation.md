There are a few ways how app can be build and run

* Built and deployed with maven on standalone tomcat - *the best fit for development*
* Built with maven and run as docker image (tomcat based) - *not require to install Tomcat*
* Built and run as docker image (maven + tomcat based) - *not require neither maven nor Tomcat*  

###### Java & Maven (if you want to build with local maven)

if you are going to use third option you can skip this chapter

Make sure you have JDK installed 

````
java -version
```` 

On my local I have ``11.0.7`` version

Make sure  you have Maven installed

````
mvn -v
````

On my local I have ``3.6.0`` version 

###### Build and deploy

You can either deploy application on tomcat application server or build a docker image

###### Build and deploy on tomcat

* Make sure you have tomcat server installed and running

you can check version with the commands
````
cd /opt/tomcat/latest/lib/

java -cp catalina.jar org.apache.catalina.util.ServerInfo
```` 
On my local I have 9.0.34 version

check if service is running and run if needed

````
sudo systemctl status tomcat
sudo systemctl start tomcat
````

* Build and deploy application with maven from app root folder

````
mvn clean tomcat7:deploy
````

Use ``redeploy`` command if you've already deployed application
````
mvn clean tomcat7:redeploy
```` 

###### Build and deploy docker image (tomcat based)

* build application with maven

````
mvn clean package
````

as a result of a successful build you should find ``target`` folder and ``camundaDemo.war`` file inside

to build application without test running 

````
mvn clean package -DskipTests
````

* build a docker image 

````
sudo docker build --rm -t mikhailbolshakov/camunda-tomcat ./ 
````

* run a container (make sure environment variables correctly set in `env.list` )
````
sudo docker run --rm -it --network=host --env-file ./env.list mikhailbolshakov/camunda-tomcat
````

###### Build and deploy docker image (tomcat + maven based)

You should use `Dockerfile.maven-build` to build image.
It will build and deploy the application without any additional software installed on your local
