package org.camunda.wf.userTask;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.common.base.BaseImpl;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.camunda.wf.messaging.builder.MessageBuilder;
import org.camunda.wf.messaging.message.UserTaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTaskCreateListenerImpl extends BaseImpl {

    private final MessageBrokerConnection messageBrokerConnection;
    private final MessageBuilder messageBuilder;
    private final ProcessEngine processEngine;

    @Autowired
    public UserTaskCreateListenerImpl(ProcessEngine processEngine,
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

    private String getTopic(DelegateTask delegateTask) {

        ProcessDefinition pd = processEngine
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(delegateTask.getProcessDefinitionId())
                .singleResult();

        return String.format("user-task.%s.%s", pd.getKey(), delegateTask.getTaskDefinitionKey());
    }

    public void notify(DelegateTask delegateTask) {

        D("taskId = %s", delegateTask.getId());

        try {

            String msg = messageBuilder
                    .withMessageType(UserTaskMessage.class)
                    .withUniqueID()
                    .withProcess(delegateTask.getProcessDefinitionId(), delegateTask.getProcessInstanceId())
                    .withTask(delegateTask.getTaskDefinitionKey(), delegateTask.getId())
                    .withVariables(delegateTask.getVariables())
                    .buildAsString();

            String topic = getTopic(delegateTask);
            publishMessage(topic, msg);

            D("Sent. Topic: %s.\n Payload: %s", topic, msg);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }
    }
}