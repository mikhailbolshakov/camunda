package org.camunda.wf.userTask;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.common.base.BaseImpl;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.common.wf.message.ExecutionContext;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;

public class UserTaskCreateListener extends BaseImpl implements TaskListener {

    protected void publishMessage(String subject, String message) throws MessageBrokerException {

        MessageBrokerConnection messageBrokerConnection = ApplicationContextProvider.getContext().getBean(MessageBrokerConnection.class);

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setSubject(subject);
        request.setMessage(message);

        messageBrokerConnection.publish(request);

    }

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

            publishMessage(delegateTask.getTaskDefinitionKey(), msg.toString());

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }
    }
}