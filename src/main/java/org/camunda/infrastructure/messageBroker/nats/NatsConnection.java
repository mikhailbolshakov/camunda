package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.*;
import io.nats.client.Message;
import org.camunda.repository.messageBroker.*;
import org.camunda.repository.messageBroker.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NatsConnection implements MessageBrokerConnection, AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(NatsConnection.class.getName());

    private final Options connectionOptions;
    private Connection connection = null;

    private final List<MessageBrokerSubscriptionProvider> subscriptionProviders;

    public NatsConnection(Options opt, List<MessageBrokerSubscriptionProvider> subscriptionProviders) {
        this.connectionOptions = opt;
        this.subscriptionProviders = subscriptionProviders;
    }

    private void catchNatsError(Throwable e, String message, boolean rethrow) throws MessageBrokerException{
        String errMsg = String.format("[NATS] %s.\n Error: %s", message, e.getMessage());
        logger.error(errMsg, e);
        if (rethrow)
            throw new MessageBrokerException(e, errMsg);
    }

    @Override
    public boolean isOpen() {
        return connection != null &&
                connection.getStatus() == Connection.Status.CONNECTED;
    }

    @Override
    public void open() throws MessageBrokerException {

        try {

            checkConnectionState(false);
            connection = Nats.connect(connectionOptions);

            // set subscription if a providers specified
            if (subscriptionProviders != null)
                for(MessageBrokerSubscriptionProvider p: subscriptionProviders){
                    p.subscribe(this);
                }

            logger.debug("[NATS] Connection opened");

        } catch (Exception e) {
            catchNatsError(e, "Connection error.", true);
        }

    }

    protected void checkConnectionState(boolean open) throws MessageBrokerException {
        if (open != isOpen())
            throw new MessageBrokerException("Connection status cannot be changed");
    }

    @Override
    public void close() throws MessageBrokerException {
        try {
            if (!isOpen())
                throw new MessageBrokerException("Can't close not active connection.");

            connection.close();

            logger.debug("[NATS] Connection closed");
        }
        catch(Exception e) {
            catchNatsError(e, "Close connection error.", true);
        }
    }

    @Override
    public void publish(MessageBrokerPublishRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            connection.publish(request.getSubject(), request.getMessage().getBytes(StandardCharsets.UTF_8));

            logger.debug(String.format("[NATS] Message published.\n Subject: %s.\n Message: %s", request.getSubject(), request.getMessage()));

        } catch (Exception e) {
            catchNatsError(e, "Publishing error.", true);
        }
    }

    protected org.camunda.repository.messageBroker.Message convertMessage(Message natsMessage) {
        org.camunda.repository.messageBroker.Message internalMsg = new org.camunda.repository.messageBroker.Message();
        internalMsg.setTopic(natsMessage.getSubject());
        internalMsg.setPayload(new String(natsMessage.getData(), StandardCharsets.UTF_8));
        return internalMsg;
    }

    protected void handlerInternal(MessageHandler handler, Message natsMsg) {

        org.camunda.repository.messageBroker.Message message = convertMessage(natsMsg);

        try {

            logger.debug(String.format("[NATS] Message received: %s", message.getPayload()));

            handler.onMessage(message);

        } catch (Exception e) {
            String errMsg = String.format("[NATS] Error.\n Message: %s.\n Error: %s", message.getPayload(), e.getMessage());
            logger.error(errMsg, e);
        }
    }

    @Override
    public void subscribe(MessageBrokerSubscribeRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            Dispatcher dispatcher = connection.createDispatcher((msg) -> handlerInternal(request.getMessageHandler(), msg));

            if(request.getAttributes().containsKey("group"))
                dispatcher.subscribe(request.getTopic(), (String)request.getAttributes().get("group"));
            else
                dispatcher.subscribe(request.getTopic());

            logger.debug(String.format("[NATS] Subscriber registered. Subject: %s", request.getTopic()));

        } catch (Exception e) {
            catchNatsError(e, "Subscriber registration error.", true);
        }

    }

    @Override
    public List<MessageBrokerSubscriptionProvider> getSubscriptionProviders() {
        return subscriptionProviders;
    }
}
