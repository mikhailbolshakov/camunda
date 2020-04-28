package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.MessageHandler;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.springframework.util.StringUtils;

public class NatsSubscribeRequest extends MessageBrokerSubscribeRequest {

    private String subject;
    public String getSubject() {
        return subject;
    }
    public void setSubject(String value) {
        subject = value;
    }

    private MessageHandler messageHandler;
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
    public void setMessageHandler(MessageHandler value) {
        messageHandler = value;
    }

    private String group;
    public String getGroup() {
        return group;
    }
    public void setGroup(String value) {
        group = value;
    }

    @Override
    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(subject) ||
           messageHandler == null)
            throw new MessageBrokerException("Subscribe request validation error");
    }
}
