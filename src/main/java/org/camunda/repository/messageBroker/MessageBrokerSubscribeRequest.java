package org.camunda.repository.messageBroker;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageBrokerSubscribeRequest {

    private Map<String, Object> attributes = new HashMap<>();
    public Map<String, Object> getAttributes() {
        return attributes;
    }

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

    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(topic) || messageHandler == null)
            throw new MessageBrokerException("Subscribe request validation error");
    }

}
