package org.camunda.bootstrapper;

import io.nats.client.Options;
import org.camunda.infrastructure.messageBroker.nats.NatsConnection;
import org.camunda.infrastructure.messageBroker.nats.NatsConnectionOptions;
import org.camunda.infrastructure.messageBroker.nats.NatsSubscribeRequest;
import org.camunda.infrastructure.messageBroker.nats.NatsMessageBroker;
import org.camunda.repository.messageBroker.MessageBroker;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.wf.serviceTask.ServiceTaskCompletionMessageHandler;
import org.camunda.wf.userTask.UserTaskCompletionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MessageBrokerConfiguration {

    private final String NATS = "nats";
    private final String MOCK = "mock";

    private final String SERVICE_TASK_COMPLETION_SUBJECT = "service-task.completion";
    private final String USER_TASK_COMPLETION_SUBJECT = "user-task.completion";

    //@Autowired
    private ServiceTaskCompletionMessageHandler serviceTaskCompletionHandler;

    //@Autowired
    private UserTaskCompletionMessageHandler userTaskCompletionHandler;

    @Autowired
    private List<MessageBroker> supportedMessageBrokers;

    @Value("${org.camunda.message-broker.type" + ":" + NATS + "}")
    private String type;

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

        MessageBroker messageBroker = null;

        for(MessageBroker mb: supportedMessageBrokers) {
            if (mb.getType().equals(type)) {
                messageBroker = mb;
                break;
            }
        }

        if (messageBroker == null)
            throw new MessageBrokerException(String.format("Message broker with type %s isn't supported", type));

        MessageBrokerConnectionOptions options = messageBroker.createOptions();
        NatsConnection connection = (NatsConnection)messageBroker.prepareConnection(options);

        connection.open();

        //setSubscription(connection);

        return connection;
    }

}
