## Installation

Make sure you have `npm` installed

````
npm -v
```` 

Install bpmnlint utility 

````
npm install -g bpmnlint
````

`-g` means global installation in `/usr/lib/node-modules`

## Check model

Make sure you have `.bpmnlintrc` file created in your folder

Otherwise run
````
bpmnlint --init
````

Check the model by running the command

````
bpmnlint ../../models/samples/auto-offline-service.bpmn 

/home/mikhailb/work/java/camunda/bpmn/models/samples/auto-offline-service.bpmn
  auto.mobile-service.service-company.service-provided.event  warning  Incoming flows do not join  fake-join
  auto.mobile-service.service-company.assign-mobile.s-task    warning  Incoming flows do not join  fake-join

✖ 2 problems (0 errors, 2 warnings)
````

## Rules

In default configuration some basic rules are applied.
You can change default behaviour by modifying the `.bpmnlintrc`  

E.g. to disable the rule checking empty lables add `rule` tag as follows

````
{
  "extends": "bpmnlint:recommended",
  "rules": {
    "label-required": "off"
  }
}
````
