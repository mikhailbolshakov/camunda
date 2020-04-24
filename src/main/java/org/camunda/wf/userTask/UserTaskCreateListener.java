package org.camunda.wf.userTask;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.common.base.BaseImpl;
import org.camunda.common.libs.messaging.nats.Nats;
import org.camunda.common.wf.message.ExecutionContext;
import org.camunda.wf.serviceTask.ServiceTaskOutgoingMessage;
import org.springframework.util.StringUtils;

import java.util.logging.Logger;

public class UserTaskCreateListener extends BaseImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        D(String.format("taskId = %s", delegateTask.getId()));

        try {

            // TODO: use factory or builder
            UserTaskOutgoingMessage msg = new UserTaskOutgoingMessage();

            ExecutionContext ctx = msg.getContext();
            ctx.setProcessDefId(delegateTask.getProcessDefinitionId());
            ctx.setProcessId(delegateTask.getProcessInstanceId());
            ctx.setVariables(delegateTask.getVariables());
            msg.setTaskId(delegateTask.getId());

            D("Send message: %s", msg.toString());

            // TODO: call through MessageBroker interface
            Nats.publish(delegateTask.getTaskDefinitionKey(), msg.toString());
        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }
    }
}