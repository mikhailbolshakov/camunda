package org.camunda.repository.messageBroker;

public interface MessageBroker {

    // prepare connection to the message broker
    MessageBrokerConnection prepareConnection(MessageBrokerConnectionOptions options) throws MessageBrokerException;
}
