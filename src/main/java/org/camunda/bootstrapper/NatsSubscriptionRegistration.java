package org.camunda.bootstrapper;

import io.nats.client.Connection;
import org.camunda.common.libs.messaging.nats.Nats;
import org.camunda.wf.serviceTask.ServiceTaskCompletionMessageHandler;
import org.camunda.wf.userTask.UserTaskCompletionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NatsSubscriptionRegistration {

    @Autowired
    private ServiceTaskCompletionMessageHandler serviceTaskCompletionHandler;

    @Autowired
    private UserTaskCompletionMessageHandler userTaskCompletionHandler;

    @PostConstruct
    public void registerSubscribers() throws Exception {
        Connection connection = Nats.connect(true);

        Nats.subscribe(connection, "service-task.completion", serviceTaskCompletionHandler);
        Nats.subscribe(connection, "user-task.completion", userTaskCompletionHandler);

    }

}
