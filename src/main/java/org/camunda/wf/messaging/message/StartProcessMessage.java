package org.camunda.wf.messaging.message;

import org.camunda.wf.messaging.common.CompletionContext;
import org.camunda.wf.messaging.common.MessageBase;
import org.camunda.wf.messaging.common.StartProcessContext;

public class StartProcessMessage extends MessageBase {

    private StartProcessContext startProcessContext;
    public StartProcessContext getStartProcessContext() {return startProcessContext;}
    public void setStartProcessContext(StartProcessContext value) {startProcessContext = value;}

}
