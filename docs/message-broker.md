## message broker usage

A message broker is used by camunda process to interact with outside world.
It sends / receives messages as a process is being performed. 
Once a task is reached a special type of message is sent to be handled by outside's business services. 
Once a business service completes its job it sends completion message to allow a task to be completed and a process moves on. 

Currently there are two types of message brokers supported: NATS and MOCK.

* **NATS** allows communication with *real* NATS message broker according to its protocol. Has to be used in production and integration tests scenarios.  
* **MOCK** allows to communicate with inprocess message broker. It might simplify developping and debug of process configuration locally without involving remote business services. 

## NATS

Configure the following env variables 

````
CAMUNDA_MESSAGE_BROKER_TYPE=nats
CAMUNDA_NATS_HOST=localhost
CAMUNDA_NATS_PORT=4222
````

## MOCK

Configure the following env variables 

````
CAMUNDA_MESSAGE_BROKER_TYPE=nats
````

To configure mock message handlers edit `src/main/resources/messageBroker/mock/mock-scenario.json` file

Note, you can use JEXL expressions (https://commons.apache.org/proper/commons-jexl/reference/syntax.html)


