package org.camunda.repository.messageBroker;

public interface MessageBroker {

    // type of message broker to be set in configuration
    String getType();

    // create an instance of options object
    MessageBrokerConnectionOptions createOptions();

    // prepare connection to the message broker
    MessageBrokerConnection prepareConnection(MessageBrokerConnectionOptions options) throws MessageBrokerException;

}
