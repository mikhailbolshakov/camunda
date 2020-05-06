package org.messageBroker.nats;

import io.nats.client.Options;
import org.camunda.infrastructure.messageBroker.nats.NatsConnection;
import org.camunda.infrastructure.messageBroker.nats.NatsConnectionOptions;
import org.camunda.infrastructure.messageBroker.nats.NatsMessageBroker;
import org.camunda.repository.messageBroker.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

/*
*
* In order to all these tests are working, NATS server must be running at nats://localhost:4222
* */

@Ignore
public class NatsMessageBrokerTest {

    private NatsConnection createAndOpenConnection(List<MessageBrokerSubscriptionProvider> subscriptionProviders) throws MessageBrokerException {

        NatsMessageBroker mb = new NatsMessageBroker(subscriptionProviders);
        NatsConnectionOptions options = new NatsConnectionOptions();

        options.setUrl(Options.DEFAULT_URL);
        options.setAllowReconnect(true);
        options.setTimeout(5000);
        options.setPingInterval(10);
        options.setReconnectWait(1000);

        NatsConnection connection = (NatsConnection)mb.prepareConnection(options);

        connection.open();

        return connection;
    }

    private NatsConnection createAndOpenConnection() throws MessageBrokerException {
        return createAndOpenConnection(null);
    }

    @Test
    public void createAndOpenConnectionTest() throws MessageBrokerException {
        try(NatsConnection c = createAndOpenConnection()) {};
    }

    @Test
    public void closeAndOpenAgainTest() throws MessageBrokerException {
        try(NatsConnection connection = createAndOpenConnection()) {
            connection.close();
            connection.open();
        }
    }

    @Test
    public void cannotCloseNotOpenedConnectionTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();
        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.close();});
    }

    @Test
    public void subscribeTest() throws MessageBrokerException {

        try(NatsConnection connection = createAndOpenConnection()) {

            MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();
            request.setTopic("test");
            request.setMessageHandler((msg) -> {});
            connection.subscribe(request);

        }

    }

    @Test
    public void subscribeWithGroupTest() throws MessageBrokerException {

        try(NatsConnection connection = createAndOpenConnection()) {

            MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();
            request.setTopic("test");
            request.setMessageHandler((msg) -> {});
            request.getAttributes().put("group", "group1");

            connection.subscribe(request);
        }

    }

    @Test
    public void cannotSubscribeNotOpenedConnectionTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();

        MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();
        request.setTopic("test");
        request.setMessageHandler((msg) -> {});

        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.subscribe(request);});
    }

    @Test
    public void cannotSubscribeWithoutSubjectTest() throws MessageBrokerException {
        NatsConnection connection = createAndOpenConnection();
        connection.close();

        MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();
        request.setMessageHandler((msg) -> {});

        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.subscribe(request);});
    }

    @Test
    public void publishTest() throws MessageBrokerException {

        try(NatsConnection connection = createAndOpenConnection()) {
            MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
            request.setSubject("test");
            request.setMessage("test-message");

            connection.publish(request);
        }

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

        try( NatsConnection connection = createAndOpenConnection() ) {

            MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();
            request.setMessage("test-message");

            Assertions.assertThrows(MessageBrokerException.class, () -> {connection.publish(request);});

        }

    }

    private class TestSubscriptionProvider implements MessageBrokerSubscriptionProvider {

        @Override
        public void subscribe(MessageBrokerConnection connection) throws MessageBrokerException {
            MessageBrokerSubscribeRequest request = new MessageBrokerSubscribeRequest();
            request.setTopic("test");
            request.setMessageHandler((msg) -> {});
            connection.subscribe(request);
        }
    }

    @Test
    public void createConnectionWithSubscriptionProvidersTest() throws MessageBrokerException {

        MessageBrokerSubscriptionProvider[] providers = { new TestSubscriptionProvider() };
        try(NatsConnection connection = createAndOpenConnection(Arrays.asList(providers))) {

        }

    }

}
