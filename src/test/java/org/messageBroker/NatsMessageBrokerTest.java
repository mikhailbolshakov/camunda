package org.messageBroker;

import io.nats.client.Options;
import org.camunda.infrastructure.messageBroker.nats.NatsConnection;
import org.camunda.infrastructure.messageBroker.nats.NatsConnectionOptions;
import org.camunda.infrastructure.messageBroker.nats.NatsMessageBroker;
import org.camunda.infrastructure.messageBroker.nats.NatsSubscribeRequest;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/*
*
* In order to all these tests are working, NATS server must be running at nats://localhost:4222
* */

@Ignore
public class NatsMessageBrokerTest {

    private NatsConnection createAndOpenConnection() throws MessageBrokerException {
        NatsMessageBroker nats = new NatsMessageBroker();
        NatsConnectionOptions options = new NatsConnectionOptions();

        options.setUrl(Options.DEFAULT_URL);
        options.setAllowReconnect(true);

        NatsConnection connection = (NatsConnection)nats.prepareConnection(options);
        connection.open();

        return connection;
    }

    @Test
    public void createAndOpenConnectionTest() throws MessageBrokerException {
        createAndOpenConnection();
    }

    @Test
    public void closeAndOpenAgainTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();
        connection.open();
    }

    @Test
    public void cannotCloseNotOpenedConnectionTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();
        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.close();});
    }

    @Test
    public void subscribeTest() throws MessageBrokerException {

        NatsConnection connection = createAndOpenConnection();

        NatsSubscribeRequest request = new NatsSubscribeRequest();
        request.setSubject("test");
        request.setMessageHandler((msg) -> {});
        connection.subscribe(request);
    }

    @Test
    public void cannotSubscribeNotOpenedConnectionTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();

        NatsSubscribeRequest request = new NatsSubscribeRequest();
        request.setSubject("test");
        request.setMessageHandler((msg) -> {});

        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.subscribe(request);});
    }

    @Test
    public void cannotSubscribeWithoutSubjectTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();

        NatsSubscribeRequest request = new NatsSubscribeRequest();
        request.setMessageHandler((msg) -> {});

        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.subscribe(request);});
    }

    @Test
    public void publishTest() throws MessageBrokerException {

        NatsConnection connection = createAndOpenConnection();

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setSubject("test");
        request.setMessage("test-message");

        connection.publish(request);

    }

    @Test
    public void cannotPublishNotOpenedConnectionTest() throws MessageBrokerException {

        NatsConnection connection = createAndOpenConnection();

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setSubject("test");
        request.setMessage("test-message");

        connection.close();
        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.publish(request);});

    }

    @Test
    public void cannotPublishWithoutSubjectTest() throws MessageBrokerException {

        NatsConnection connection = createAndOpenConnection();

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
        request.setMessage("test-message");

        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.publish(request);});

    }

}
