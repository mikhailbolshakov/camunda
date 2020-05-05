package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.ConnectionListener;
import io.nats.client.ErrorListener;
import io.nats.client.Options;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NatsConnectionOptions extends MessageBrokerConnectionOptions {

    @Value("${org.camunda.message-broker.nats.url}")
    private String url;
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return url; }

    @Value("${org.camunda.message-broker.nats.allow-reconnect}")
    private boolean allowReconnect;
    public void setAllowReconnect(boolean allowReconnect) { this.allowReconnect = allowReconnect; }
    public boolean getAllowReconnect() { return allowReconnect; }

    @Value("${org.camunda.message-broker.nats.timeout}")
    private int timeout;
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getTimeout() {
        return timeout;
    }

    @Value("${org.camunda.message-broker.nats.ping-interval}")
    private int pingInterval;
    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }
    public int getPingInterval() {
        return pingInterval;
    }

    @Value("${org.camunda.message-broker.nats.reconnect-wait}")
    private int reconnectWait;
    public void setReconnectWait(int reconnectWait) {
        this.reconnectWait = reconnectWait;
    }
    public int getReconnectWait() {
        return reconnectWait;
    }

    private ErrorListener errorListener;
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
    public ErrorListener getErrorListener() {
        return errorListener;
    }

    private ConnectionListener connectionListener;
    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    @Override
    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(url))
            throw new MessageBrokerException("Connection option validation error");
    }

}
