package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;

public class HandlerSubscriber implements Subscriber {

    private final MessageHandler handler;
    private final String topic;

    public HandlerSubscriber(String topic, MessageHandler handler) {
        this.topic = topic;
        this.handler = handler;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void Consume(Message message){
        handler.onMessage(message);
    }
}
