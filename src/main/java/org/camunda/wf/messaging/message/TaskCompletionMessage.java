package org.camunda.wf.messaging.message;

import org.camunda.wf.messaging.common.CompletionContext;
import org.camunda.wf.messaging.common.Header;
import org.camunda.wf.messaging.common.MessageBase;

import java.util.Map;

public class TaskCompletionMessage extends MessageBase {

    private CompletionContext completionContext;
    public CompletionContext getCompletionContext() {return completionContext;}
    public void setCompletionContext(CompletionContext value) {completionContext = value;}

}
