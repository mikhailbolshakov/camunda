package org.camunda.infrastructure.messageBroker.mockBroker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.HandlerSubscriber;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MessageProcessor;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MockSubscriber;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.Subscriber;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class MockConnection implements MessageBrokerConnection, AutoCloseable {

    private static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private List<JsonObject> rules;

    private final BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();
    private final CopyOnWriteArrayList<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final MessageProcessor processor = new MessageProcessor(messageQueue, subscribers);

    private boolean isOpen = false;

    public MockConnection(List<JsonObject> rules) throws MessageBrokerException {
        this.rules = rules;
        registerMockSubscribers();
    }

    private void registerMockSubscribers() throws MessageBrokerException {

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

            if (!(request instanceof MockSubscribeRequest))
                throw new MessageBrokerException("Unproper request type: %s", request.getClass().getName());

            request.validate();

            MockSubscribeRequest rq = (MockSubscribeRequest) request;

            Subscriber s = new HandlerSubscriber(rq.getTopic(), rq.getMessageHandler());
            subscribers.add(s);

            logger.debug(String.format("[MockMessageBroker] Consumer registered. Subject: %s", rq.getTopic()));

        } catch (Exception e) {
            catchError(e, "Subscriber registration error.", true);
        }

    }

    public boolean notProcessedMessagesExist() {
        return !processor.isQueueEmpty();
    }
}
