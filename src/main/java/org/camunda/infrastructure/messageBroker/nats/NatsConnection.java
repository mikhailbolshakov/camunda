package org.camunda.infrastructure.messageBroker.nats;

import io.nats.client.*;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.camunda.repository.messageBroker.MessageBrokerSubscribeRequest;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

public class NatsConnection implements MessageBrokerConnection {

    protected static Logger logger = LoggerFactory.getLogger(NatsConnection.class.getName());

    protected Options connectionOptions;
    protected Connection connection = null;

    public NatsConnection(Options opt) {
        this.connectionOptions = opt;
    }

    protected void catchNatsError(Throwable e, String message, boolean rethrow) throws MessageBrokerException{
        String errMsg = String.format("[NATS] %s.\n Error: %s", message, e.getMessage());
        logger.error(errMsg, e);
        if (rethrow)
            throw new MessageBrokerException(e, errMsg);
    }

    @Override
    public boolean isOpen() {
        return connection != null &&
                connection.getStatus() == Connection.Status.CONNECTED;
    }

    @Override
    public void open() throws MessageBrokerException {

        try {

            checkConnectionState(false);
            connection = Nats.connect(connectionOptions);

        } catch (Exception e) {
            catchNatsError(e, "Connection error.", true);
        }

    }

    protected void checkConnectionState(boolean open) throws MessageBrokerException {
        if (open != isOpen())
            throw new MessageBrokerException("Connection isn't opened");
    }

    @Override
    public void close() throws MessageBrokerException {
        try {
            if (!isOpen())
                throw new MessageBrokerException("Can't close not active connection.");

            connection.close();
        }
        catch(Exception e) {
            catchNatsError(e, "Close connection error.", true);
        }
    }

    @Override
    public void publish(MessageBrokerPublishRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            connection.publish(request.getSubject(), request.getMessage().getBytes(StandardCharsets.UTF_8));

            logger.debug(String.format("[NATS] Message sent.\n Subject: %s.\n Message: %s", request.getSubject(), request.getMessage()));

        } catch (Exception e) {
            catchNatsError(e, "Publishing error.", true);
        }
    }

    protected void handlerInternal(MessageHandler handler, Message msg) {

        String msgTxt = "";

        try {
            msgTxt = new String(msg.getData(), StandardCharsets.UTF_8);

            logger.debug(String.format("[NATS] Message received: %s", msgTxt));

            handler.onMessage(msg);

        } catch (Exception e) {
            String errMsg = String.format("[NATS] %s.\n Message: %s.\n Error: %s", msgTxt, e.getMessage());
            logger.error(errMsg, e);
        }
    }

    @Override
    public void subscribe(MessageBrokerSubscribeRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            if (!(request instanceof NatsSubscribeRequest))
                throw new MessageBrokerException("Unproper request type: %s", request.getClass().getName());

            request.validate();

            NatsSubscribeRequest rq = (NatsSubscribeRequest) request;

            Dispatcher dispatcher = connection.createDispatcher((msg) -> handlerInternal(rq.getMessageHandler(), msg));

            if(!StringUtils.isEmpty(rq.getGroup()))
                dispatcher.subscribe(rq.getSubject(), rq.getGroup());
            else
                dispatcher.subscribe(rq.getSubject());

            logger.debug(String.format("[NATS] Consumer registered. Subject: %s", rq.getSubject()));

        } catch (Exception e) {
            catchNatsError(e, "Subscriber registration error.", true);
        }

    }
}
