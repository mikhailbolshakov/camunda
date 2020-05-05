package org.camunda.infrastructure.messageBroker.mockBroker;

import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.springframework.util.StringUtils;

public class MockSubscribeRequest extends MessageBrokerSubscribeRequest {

    private String topic;
    public String getTopic() {
        return topic;
    }
    public void setTopic(String value) {
        topic = value;
    }

    private MessageHandler messageHandler;
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
    public void setMessageHandler(MessageHandler value) {
        messageHandler = value;
    }

    @Override
    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(topic) ||
           messageHandler == null)
            throw new MessageBrokerException("Subscribe request validation error");
    }
}
