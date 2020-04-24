package org.camunda.wf.userTask;

import org.camunda.common.wf.message.MessageBase;

import java.util.Map;

public class UserTaskCompleteIncomingMessage extends MessageBase {

    private String taskId;
    public String getTaskId() {return taskId;}
    public void setTaskId(String value) {taskId = value;}

    private Map<String, Object> variables;
    public Map<String, Object> getVariables() {return variables;}
    public void setVariables(Map<String, Object> value) {variables = value;}

}
