package org.camunda.repository.messageBroker;

public abstract class MessageBrokerConnectionOptions {

    public abstract void validate() throws MessageBrokerException;

}
