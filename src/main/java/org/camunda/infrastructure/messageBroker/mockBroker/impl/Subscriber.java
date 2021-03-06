package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageBrokerException;

public interface Subscriber {

    String getTopic();

    void Consume(Message message) throws MessageBrokerException, InterruptedException;
}
