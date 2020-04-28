package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.ConnectionListener;
import io.nats.client.ErrorListener;
import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;

public class NatsConnectionOptions extends MessageBrokerConnectionOptions {

    private boolean allowReconnect = true;
    public void setAllowReconnect(boolean allowReconnect) {
        this.allowReconnect = allowReconnect;
    }
    public boolean getAllowReconnect() {
        return allowReconnect;
    }

    private int timeout = 5000;
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getTimeout() {
        return timeout;
    }

    private int pingInterval = 10;
    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }
    public int getPingInterval() {
        return pingInterval;
    }

    private int reconnectWait = 1000;
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
        super.validate();

    }

}
