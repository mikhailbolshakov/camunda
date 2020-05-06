package org.camunda.repository.messageBroker;

public interface MessageHandler {
    void onMessage(Message msg);
}