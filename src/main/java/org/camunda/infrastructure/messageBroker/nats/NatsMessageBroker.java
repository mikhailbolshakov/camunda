package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.*;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBroker;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class NatsMessageBroker implements MessageBroker {

    private final String NATS_BROKER_TYPE = "nats";

    protected static Logger logger = LoggerFactory.getLogger(NatsMessageBroker.class.getName());

    @Override
    public String getType() {
        return NATS_BROKER_TYPE;
    }

    @Override
    public MessageBrokerConnectionOptions createOptions() {
        return ApplicationContextProvider.getContext().getBean(NatsConnectionOptions.class);
    }

    @Override
    public MessageBrokerConnection prepareConnection(MessageBrokerConnectionOptions options) throws MessageBrokerException {

        try {

            if(!(options instanceof NatsConnectionOptions))
                throw new MessageBrokerException("Unproper options type");

            options.validate();

            NatsConnectionOptions op = (NatsConnectionOptions)options;

            Options.Builder builder = new Options.Builder().
                    server(op.getUrl()).
                    connectionTimeout(Duration.ofSeconds(op.getTimeout())).
                    pingInterval(Duration.ofSeconds(op.getPingInterval())).
                    reconnectWait(Duration.ofSeconds(op.getReconnectWait()));

            if(op.getErrorListener() != null)
                builder.errorListener(op.getErrorListener());

            if(op.getConnectionListener() != null)
                builder.connectionListener(op.getConnectionListener());

            if (!op.getAllowReconnect()) {
                builder = builder.noReconnect();
            } else {
                builder = builder.maxReconnects(-1);
            }

            return new NatsConnection(builder.build());
        }
        catch(Exception e) {
            String errMsg = String.format("[NATS] Prepare connection error. Error: %s", e.getMessage());
            logger.error(errMsg, e);
            throw new MessageBrokerException(e, errMsg);
        }

    }

}
