package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.camunda.wf.messaging.builder.MessageBuilder;
import org.camunda.wf.messaging.message.ServiceTaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTaskDelegateImpl extends BaseImpl {

    private final MessageBrokerConnection messageBrokerConnection;
    private final MessageBuilder messageBuilder;
    private final ProcessEngine processEngine;

    @Autowired
    public ServiceTaskDelegateImpl(ProcessEngine processEngine,
                                   MessageBrokerConnection messageBrokerConnection,
                                   MessageBuilder messageBuilder) {
        this.messageBrokerConnection = messageBrokerConnection;
        this.messageBuilder = messageBuilder;
        this.processEngine = processEngine;
    }

    private void publishMessage(String subject, String message) throws MessageBrokerException {

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setSubject(subject);
        request.setMessage(message);

        messageBrokerConnection.publish(request);

    }

    private String getTopic(ActivityExecution execution) {

        ProcessDefinition pd = processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(execution.getProcessDefinitionId())
                .singleResult();

        return String.format("service-task.%s.%s", pd.getKey(), execution.getCurrentActivityId());
    }

    public void execute(ActivityExecution execution) {

        D("executionId = %s", execution.getId());

        try {

            String msg = messageBuilder
                    .withMessageType(ServiceTaskMessage.class)
                    .withUniqueID()
                    .withProcess(execution.getProcessDefinitionId(), execution.getProcessInstanceId())
                    .withTask(execution.getCurrentActivityId(), execution.getId())
                    .withVariables(execution.getVariables())
                    .buildAsString();

            String topic = getTopic(execution);
            publishMessage(topic, msg);

            D("Sent. Topic: %s.\n Payload: %s", topic, msg);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }

}