## How to install and run

#### Repository

````
git clone https://github.com/mikhailbolshakov/camunda.git
````

#### Database

For application to successfully start you need a database configured and run

Although, variety of databases is supported now we are playing around with MySQL

Please, refer to [database reference](./docs/database.md) for more info

#### Application configuration

Check ``src/main/resources/application.properties`` configuration file and correct database url if needed

It uses Mock message broker by default which is OK for the first run

#### Application build and deploy 

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


#### Message broker configuration

To run the process a message broker must be properly configured.
Please, refer to [message broker reference](./docs/message-broker.md)

#### Deployment models

To run the process you have to deploy a process model first
Please, refer to [message broker reference](./docs/message-broker.md)

#### Run the process

If all steps have been successfully completed you can browse a camunda web app on ``http://localhost:8080/camundaDemo``

Then you can run a test process by sending POST request with the parameters

http://localhost:8080/camundaDemo/custom-api/process/instance

````json
{
	"processKey": "auto.mobile-service.platform.process",
	"variables": {
		"clientUserId": "client1"	
	}
}
````
 
as a result you should get the following response

````json
{
    "processInstanceId": "23",
    "rootBusinessKey": "8d9cc239-ee24-48ad-ad58-50c6a6bcf62e"
}
````

then you can check the process state by the executing the query

````sql
select  p.ID_,
		p.PROC_DEF_KEY_,
		p.STATE_,
		t.NAME_ as task_name,
		CONCAT(usr.FIRST_, ' ', usr.LAST_) assignee,
		grp.NAME_,
		case when t.END_TIME_ is null then 'in progress' else 'finished' end status, 
		t.DURATION_ 
  from ACT_HI_PROCINST p
  	left join ACT_HI_TASKINST t on p.ID_  = t.PROC_INST_ID_ 
  	left join ACT_ID_USER  usr on t.ASSIGNEE_  = usr.ID_ 
  	left join ACT_ID_MEMBERSHIP  ms on usr.ID_ = ms.USER_ID_ 
  	left join ACT_ID_GROUP  grp on ms.GROUP_ID_ = grp.ID_
  	where p.BUSINESS_KEY_  = [business-key]
  	order by t.START_TIME_ 
````

#### Logging

Please, refer to [logging reference](./docs/logging.md)


