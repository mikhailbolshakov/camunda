## Download

Download modeler from the [camunda official resource](https://downloads.camunda.cloud/release/camunda-modeler/3.7.3/camunda-modeler-3.7.3-linux-x64.tar.gz
) 
 
## Install and run

Unpack to any folder and run the binary file

````
./camunda-modeler
````

## How to use

Refer to the [official doc](https://docs.camunda.org/manual/latest/modeler/) to get more info 

## Plugins

To install a plugin to Camunda Modeler you need to add a folder to `{MODELER_PATH}/resources/plugins` and restart modeler

Some useful plugins provided by Camunda team
* [lint](https://github.com/camunda/camunda-modeler-linter-plugin.git) - checks the model while modeling and shows errors / warnings
* [task resize](https://github.com/philippfromme/camunda-modeler-plugin-resize-tasks.git) - allows to resize a task shape on a diagram 
* [tooltips](https://github.com/viadee/camunda-modeler-tooltip-plugin.git)

More info about Modelere plugins [here](https://github.com/camunda/camunda-modeler-plugins)

## Templates

Templates are used to simplify modeling by manipulation of preconfigured model elements.

To install current templates run form rpojetc root directory

````
cp -r ./bpmn/tools/modeler/resources/element-templates ${CAMUNDA_MODELER_PATH}/resources
````

where ${CAMUNDA_MODELER_PATH} is absolute path to camunda modeler directory

  
