package org.camunda.repository.messageBroker;

import org.springframework.util.StringUtils;

public abstract class MessageBrokerConnectionOptions {

    private String url;
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(url))
            throw new MessageBrokerException("Connection option validation error");
    }

}
