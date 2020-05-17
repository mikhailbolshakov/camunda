package org.camunda.infrastructure.wf.messageBrokerSubscription;

import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.camunda.repository.messageBroker.MessageBrokerSubscriptionProvider;
import org.camunda.wf.serviceTask.ServiceTaskCompletionHandler;
import org.camunda.wf.userTask.UserTaskCompletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkFlowSubscriptionProvider implements MessageBrokerSubscriptionProvider {

    private final String SERVICE_TASK_COMPLETION_SUBJECT = "service-task.completion";
    private final String USER_TASK_COMPLETION_SUBJECT = "user-task.completion";

    @Autowired
    private ServiceTaskCompletionHandler serviceTaskCompletionHandler;

    @Autowired
    private UserTaskCompletionHandler userTaskCompletionHandler;

    @Override
    public void subscribe(MessageBrokerConnection connection) throws MessageBrokerException {

        if (!connection.isOpen())
            throw new MessageBrokerException("Cannot subscribe on closed connection");

        MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();

        request.setTopic(SERVICE_TASK_COMPLETION_SUBJECT);
        request.setMessageHandler(serviceTaskCompletionHandler);
        connection.subscribe(request);

        request = new MessageBrokerSubscribeRequest();
        request.setTopic(USER_TASK_COMPLETION_SUBJECT);
        request.setMessageHandler(userTaskCompletionHandler);
        connection.subscribe(request);
    }
}
