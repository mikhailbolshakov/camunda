package org.camunda.repository.messageBroker;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageBrokerPublishRequest {

    protected String subject;
    protected String message;
    protected Map<String, Object> additionalAttrs = new HashMap<String, Object>();

    public String getSubject() {
        return subject;
    }

    public void setSubject(String value) {
        subject = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    public Object getAttr(String key) {
        return additionalAttrs.get(key);
    }

    public void setAttr(String key, Object value) {
        additionalAttrs.put(key, value);
    }

    public void validate() throws MessageBrokerException {
        if (StringUtils.isEmpty(getSubject()) ||
                StringUtils.isEmpty(getMessage()))
            throw new MessageBrokerException("Publish request validation error");
    }

}
