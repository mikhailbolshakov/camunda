package org.camunda.wf.serviceTask;

import org.camunda.common.wf.message.MessageBase;

import java.util.Map;

public class ServiceTaskCompleteIncomingMessage extends MessageBase {

    private String taskExecutionId;
    public String getTaskExecutionId() {return taskExecutionId;}
    public void setTaskExecutionId(String value) {taskExecutionId = value;}

    private Map<String, Object> variables;
    public Map<String, Object> getVariables() {return variables;}
    public void setVariables(Map<String, Object> value) {variables = value;}

}
