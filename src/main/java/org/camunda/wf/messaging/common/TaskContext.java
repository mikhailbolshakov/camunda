package org.camunda.wf.messaging.common;

public class TaskContext {
    private String taskDefId;
    public String getTaskDefId() {return taskDefId;}
    public void setTaskDefId(String value) { taskDefId = value;}

    private String taskId;
    public String getTaskId() {return taskId;}
    public void setTaskId(String value) { taskId = value;}

}
