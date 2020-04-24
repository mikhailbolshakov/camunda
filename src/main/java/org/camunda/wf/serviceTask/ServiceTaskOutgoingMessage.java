package org.camunda.wf.serviceTask;

import org.camunda.common.wf.message.ExecutionContext;
import org.camunda.common.wf.message.MessageBase;
import org.camunda.common.wf.message.MessageHeader;

public class ServiceTaskOutgoingMessage extends MessageBase {

    private String taskExecutionId;
    public String getTaskExecutionId() {return taskExecutionId;}
    public void setTaskExecutionId(String value) {taskExecutionId = value;}

    // TODO: use factory / builder
    public ServiceTaskOutgoingMessage() {
        setHeader(new MessageHeader());
        setContext(new ExecutionContext());
    }

}
