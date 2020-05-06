package org.camunda.repository.messageBroker;

public interface MessageBrokerSubscriptionProvider {

    void subscribe(MessageBrokerConnection connection) throws MessageBrokerException;

}
