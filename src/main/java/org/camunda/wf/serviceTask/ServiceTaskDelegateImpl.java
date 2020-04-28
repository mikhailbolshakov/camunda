package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.common.base.BaseImpl;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.common.wf.message.ExecutionContext;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;

public class ServiceTaskDelegateImpl extends BaseImpl {

    protected void publishMessage(String subject, String message) throws MessageBrokerException {

        MessageBrokerConnection messageBrokerConnection = ApplicationContextProvider.getContext().getBean(MessageBrokerConnection.class);

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setSubject(subject);
        request.setMessage(message);

        messageBrokerConnection.publish(request);

    }

    public void execute(ActivityExecution execution) throws Exception {

        D(String.format("executionId = %s", execution.getId()));

        try {

            // TODO: use factory or builder
            ServiceTaskOutgoingMessage msg = new ServiceTaskOutgoingMessage();

            ExecutionContext ctx = msg.getContext();
            ctx.setProcessDefId(execution.getProcessDefinitionId());
            ctx.setProcessId(execution.getProcessInstanceId());
            ctx.setVariables(execution.getVariables());
            msg.setTaskExecutionId(execution.getId());

            D("Send message: %s", msg.toString());

            publishMessage(execution.getCurrentActivityId(), msg.toString());

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }

}