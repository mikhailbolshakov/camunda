package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.camunda.repository.messageBroker.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessorMainThreadImpl implements MessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private final List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void process(Message message)  {

        for(Subscriber sub: subscribers) {

            try {

                if(sub.getTopic().equals(message.getTopic())) {
                    logger.debug(String.format("Handle message.\n Subscriber: %s. Topic: %s. Message: %s", sub.getClass().getSimpleName(), message.getTopic(), message.getPayload()));
                    sub.Consume(message);
                }

            }
            catch(Exception e){
                String errMsg = String.format("[MockMessageBroker] Handle message error.\n Error: %s", e.getMessage());
                logger.error(errMsg, e);
            }

        }

    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscribers() {
        subscribers.clear();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

}
