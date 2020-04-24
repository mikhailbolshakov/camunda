package org.camunda.wf.userTask;

import org.camunda.common.wf.message.ExecutionContext;
import org.camunda.common.wf.message.MessageBase;
import org.camunda.common.wf.message.MessageHeader;

public class UserTaskOutgoingMessage extends MessageBase {

    private String taskId;
    public String getTaskId() {return taskId;}
    public void setTaskId(String value) {taskId = value;}

    // TODO: use factory / builder
    public UserTaskOutgoingMessage() {
        setHeader(new MessageHeader());
        setContext(new ExecutionContext());
    }

}
