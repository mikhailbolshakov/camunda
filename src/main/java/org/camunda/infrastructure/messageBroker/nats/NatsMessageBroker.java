package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.*;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBroker;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public class NatsMessageBroker implements MessageBroker {

    protected static Logger logger = LoggerFactory.getLogger(NatsMessageBroker.class.getName());

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
