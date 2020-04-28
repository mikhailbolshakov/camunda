package org.camunda.repository.messageBroker;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageBrokerSubscribeRequest {

    protected Map<String, Object> additionalAttrs = new HashMap<String, Object>();

    public Object getAttr(String key) {
        return additionalAttrs.get(key);
    }

    public void setAttr(String key, Object value) {
        additionalAttrs.put(key, value);
    }

    public abstract void validate() throws MessageBrokerException;

}
