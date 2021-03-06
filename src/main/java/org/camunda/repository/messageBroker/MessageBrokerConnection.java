package org.camunda.repository.messageBroker;

import java.util.List;

public interface MessageBrokerConnection {

    // is connection open
    boolean isOpen();

    // open connection
    void open() throws MessageBrokerException;

    // close connection
    void close() throws MessageBrokerException;

    // publish message to the message broker
    void publish(MessageBrokerPublishRequest request) throws MessageBrokerException;

    // subscribe to messages
    void subscribe(MessageBrokerSubscribeRequest request) throws MessageBrokerException;

    // subscription providers
    List<MessageBrokerSubscriptionProvider> getSubscriptionProviders();

}
