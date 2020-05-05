package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import com.google.gson.JsonObject;
import org.camunda.infrastructure.messageBroker.mockBroker.Message;
import org.camunda.infrastructure.messageBroker.mockBroker.MessageHandler;

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
