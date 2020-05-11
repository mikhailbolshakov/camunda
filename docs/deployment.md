## deployment

* specify env variable with a folder absolute path where your deployed model files are located

````
CAMUNDA_DEPLOYMENT_SOURCE=/var/camunda/deployment
````

if running as a docker container make sure you have `volume` correctly defined on this target folder

* copy models to be deployed to the specified folder

you can use either `bpmn` files or archive files (`zip`)

* send a POST request to camunda server

````
curl -d '{"folder": "/var/camunda", "items": [{"deploymentName": "process","fileName": "process.bpmn"}]}' -H "Content-Type: application/json" -X POST http://localhost:8080/camundaDemo/custom-api/deployment/deploy
````
make sure you specify a container folder mounted to your host folder

As a result you will get a report about deployments, like

````json
{"items":[{"deploymentName":"process","deploymentId":"2101","deploymentTime":"2020-05-11T14:33:16.494+0000","error":null}]}
````
  