package org.camunda.wf.messaging.common;

public class TaskMessage extends MessageBase {

    private TaskContext taskContext;
    public TaskContext getTaskContext() {return taskContext;}
    public void setTaskContext(TaskContext value) { taskContext = value;}

    private ProcessContext processContext;
    public ProcessContext getProcessContext() {return processContext;}
    public void setProcessContext(ProcessContext value) { processContext = value;}


}
