package org.camunda.repository.wf;

public interface MessageBroker {

    // publish message to the message broker
    void publish(String topic, Object message);

    // subscribe to messages
    void subscribe(String topic, Object delegate);
}
