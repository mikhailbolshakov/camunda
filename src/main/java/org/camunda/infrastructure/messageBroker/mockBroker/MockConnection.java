package org.camunda.infrastructure.messageBroker.mockBroker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.HandlerSubscriber;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MessageProcessor;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MockSubscriber;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.Subscriber;
import org.camunda.repository.messageBroker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class MockConnection implements MessageBrokerConnection, AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private final List<JsonObject> rules;

    private final BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();
    private final CopyOnWriteArrayList<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final MessageProcessor processor = new MessageProcessor(messageQueue, subscribers);

    private final List<MessageBrokerSubscriptionProvider> subscriptionProviders;

    private boolean isOpen = false;

    public MockConnection(List<JsonObject> rules,
                          List<MessageBrokerSubscriptionProvider> subscriptionProviders) throws MessageBrokerException {
        this.rules = rules;
        this.subscriptionProviders = subscriptionProviders;

        registerMockSubscribers();
    }

    private void registerMockSubscribers() throws MessageBrokerException {

        logger.debug(String.format("[MockMessageBroker] Mock subscribers registration. Found %d ones", rules.size()));

        for(JsonObject rule: rules) {

            for (JsonElement s : rule.getAsJsonArray("subscribers")) {

                if(!s.isJsonObject())
                    throw new MessageBrokerException("Rule parsing error");

                JsonObject subscriber = s.getAsJsonObject();

                String topic = subscriber.get("topic").getAsString();
                JsonObject handler = subscriber.get("handler").getAsJsonObject();

                MockSubscriber ms = new MockSubscriber(this, topic, handler);

                ms.validateHandler();

                subscribers.add(ms);

                logger.debug(String.format("[MockMessageBroker] %s - Mock subscriber registered", topic));

            }

        }

    }

    private void catchError(Throwable e, String message, boolean rethrow) throws MessageBrokerException{
        String errMsg = String.format("[MockMessageBroker] %s.\n Error: %s", message, e.getMessage());
        logger.error(errMsg, e);
        if (rethrow)
            throw new MessageBrokerException(e, errMsg);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void open() throws MessageBrokerException {

        try {

            checkConnectionState(false);

            processor.start();

            isOpen = true;

            // set subscription if a providers specified
            if (subscriptionProviders != null)
                for(MessageBrokerSubscriptionProvider p: subscriptionProviders){
                    p.subscribe(this);
                }

            logger.debug("[MockMessageBroker] Connection opened");

        }
        catch (Exception e) {
            catchError(e, "Connection error.", true);
        }

    }

    protected void checkConnectionState(boolean open) throws MessageBrokerException {
        if (open != isOpen())
            throw new MessageBrokerException("Connection isn't opened");
    }

    @Override
    public void close() throws MessageBrokerException {
        try {

            if (!isOpen())
                throw new MessageBrokerException("Can't close not active connection.");

            processor.interrupt();

            isOpen = false;

            logger.debug("[MockMessageBroker] Connection closed");

        }
        catch(Exception e) {
            catchError(e, "Close connection error.", true);
        }
    }

    @Override
    public void publish(MessageBrokerPublishRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            Message msg = new Message();
            msg.setTopic(request.getSubject());
            msg.setPayload(request.getMessage());

            messageQueue.put(msg);

            logger.debug(String.format("[MockMessageBroker] Message published.\n Subject: %s.\n Message: %s", request.getSubject(), request.getMessage()));

        }
        catch (Exception e) {
            catchError(e, "Publishing error.", true);
        }
    }

    @Override
    public void subscribe(MessageBrokerSubscribeRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            Subscriber s = new HandlerSubscriber(request.getTopic(), request.getMessageHandler());
            subscribers.add(s);

            logger.debug(String.format("[MockMessageBroker] Subscriber registered. Subject: %s", request.getTopic()));

        } catch (Exception e) {
            catchError(e, "Subscriber registration error.", true);
        }

    }

    @Override
    public List<MessageBrokerSubscriptionProvider> getSubscriptionProviders() {
        return subscriptionProviders;
    }

    public boolean notProcessedMessagesExist() {
        return !processor.isQueueEmpty();
    }
}
