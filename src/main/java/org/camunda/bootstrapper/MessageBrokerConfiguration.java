package org.camunda.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.camunda.infrastructure.messageBroker.mockBroker.MockMessageBroker;
import org.camunda.repository.messageBroker.MessageBroker;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MessageBrokerConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(MessageBrokerConfiguration.class.getName());

    @Autowired
    private List<MessageBroker> supportedMessageBrokers;

    @Value("${org.camunda.message-broker.type}")
    private String type;

    @Bean
    public MessageBrokerConnection createMessageBrokerConnection() throws MessageBrokerException {

        logger.debug("Message broker configuration");

        MessageBroker messageBroker = null;

        for(MessageBroker mb: supportedMessageBrokers) {
            if (mb.getType().equals(type)) {
                logger.debug(String.format("Message broker found: %s", mb.getType()));
                messageBroker = mb;
                break;
            }
        }

        if (messageBroker == null)
            throw new MessageBrokerException(String.format("Message broker with type %s isn't supported", type));

        MessageBrokerConnectionOptions options = messageBroker.createOptions();
        MessageBrokerConnection connection = messageBroker.prepareConnection(options);

        logger.debug(String.format("Message broker with type %s used \n Options: %s", messageBroker.getType(), new Gson().toJson(options).toString()));

        connection.open();

        return connection;
    }

}
