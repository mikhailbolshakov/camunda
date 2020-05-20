package org.camunda.infrastructure.wf.messageBrokerSubscription;

import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.camunda.repository.messageBroker.MessageBrokerSubscriptionProvider;
import org.camunda.wf.event.StartProcessHandler;
import org.camunda.wf.serviceTask.ServiceTaskCompletionHandler;
import org.camunda.wf.userTask.UserTaskCompletionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkFlowSubscriptionProvider implements MessageBrokerSubscriptionProvider {

    private final String SERVICE_TASK_COMPLETION_TOPIC = "camunda.service-task.completion";
    private final String USER_TASK_COMPLETION_TOPIC = "camunda.user-task.completion";
    private final String START_PROCESS_TOPIC = "camunda.start-process";

    private final ServiceTaskCompletionHandler serviceTaskCompletionHandler;
    private final UserTaskCompletionHandler userTaskCompletionHandler;
    private final StartProcessHandler startProcessHandler;

    @Autowired
    public WorkFlowSubscriptionProvider(ServiceTaskCompletionHandler serviceTaskCompletionHandler,
                                        UserTaskCompletionHandler userTaskCompletionHandler,
                                        StartProcessHandler startProcessHandler) {
        this.serviceTaskCompletionHandler = serviceTaskCompletionHandler;
        this.userTaskCompletionHandler = userTaskCompletionHandler;
        this.startProcessHandler = startProcessHandler;
    }

    @Override
    public void subscribe(MessageBrokerConnection connection) throws MessageBrokerException {

        if (!connection.isOpen())
            throw new MessageBrokerException("Cannot subscribe on closed connection");

        MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();

        request.setTopic(SERVICE_TASK_COMPLETION_TOPIC);
        request.setMessageHandler(serviceTaskCompletionHandler);
        connection.subscribe(request);

        request = new MessageBrokerSubscribeRequest();
        request.setTopic(USER_TASK_COMPLETION_TOPIC);
        request.setMessageHandler(userTaskCompletionHandler);
        connection.subscribe(request);

        request = new MessageBrokerSubscribeRequest();
        request.setTopic(START_PROCESS_TOPIC);
        request.setMessageHandler(startProcessHandler);
        connection.subscribe(request);

    }
}
