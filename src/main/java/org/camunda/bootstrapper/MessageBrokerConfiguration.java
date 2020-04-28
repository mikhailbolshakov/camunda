package org.camunda.bootstrapper;

import io.nats.client.Options;
import org.camunda.infrastructure.messageBroker.nats.NatsConnection;
import org.camunda.infrastructure.messageBroker.nats.NatsConnectionOptions;
import org.camunda.infrastructure.messageBroker.nats.NatsSubscribeRequest;
import org.camunda.infrastructure.messageBroker.nats.NatsMessageBroker;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.wf.serviceTask.ServiceTaskCompletionMessageHandler;
import org.camunda.wf.userTask.UserTaskCompletionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageBrokerConfiguration {

    private final String SERVICE_TASK_COMPLETION_SUBJECT = "service-task.completion";
    private final String USER_TASK_COMPLETION_SUBJECT = "user-task.completion";

    @Autowired
    private ServiceTaskCompletionMessageHandler serviceTaskCompletionHandler;

    @Autowired
    private UserTaskCompletionMessageHandler userTaskCompletionHandler;

    private void setSubscription(NatsConnection connection) throws MessageBrokerException {

        NatsSubscribeRequest request = new NatsSubscribeRequest();

        request.setSubject(SERVICE_TASK_COMPLETION_SUBJECT);
        request.setMessageHandler(serviceTaskCompletionHandler);
        connection.subscribe(request);

        request = new NatsSubscribeRequest();
        request.setSubject(USER_TASK_COMPLETION_SUBJECT);
        request.setMessageHandler(userTaskCompletionHandler);
        connection.subscribe(request);

    }

    @Bean
    public MessageBrokerConnection createMessageBrokerConnection() throws MessageBrokerException {

        NatsMessageBroker nats = new NatsMessageBroker();
        NatsConnectionOptions options = new NatsConnectionOptions();

        // TODO: get settings from configuration file
        options.setUrl(Options.DEFAULT_URL);
        options.setAllowReconnect(true);

        NatsConnection connection = (NatsConnection)nats.prepareConnection(options);
        connection.open();

        setSubscription(connection);

        return connection;
    }

}
