package org.camunda.wf.messaging.message;

import org.camunda.wf.messaging.common.TaskMessage;
import org.camunda.wf.messaging.common.UserContext;

public class UserTaskMessage extends TaskMessage {

    private UserContext userContext;
    public UserContext getUserContext() {return userContext;}
    public void setUserContext(UserContext value) { userContext = value;}

}
