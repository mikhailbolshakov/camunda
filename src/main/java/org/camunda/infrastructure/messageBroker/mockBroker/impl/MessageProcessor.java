package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.repository.messageBroker.Message;

public interface MessageProcessor {

    void start();
    void stop();

    void process(Message message);

    void addSubscriber(Subscriber subscriber);
    void removeSubscribers();
}
