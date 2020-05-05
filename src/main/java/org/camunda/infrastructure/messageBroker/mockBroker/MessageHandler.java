package org.camunda.infrastructure.messageBroker.mockBroker;

public interface MessageHandler {
    void onMessage(Message msg);
}