package org.messageBroker.mock;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.camunda.infrastructure.messageBroker.mockBroker.*;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

//@Ignore
public class MockMessageBrokerTest {

    private static volatile boolean done = false;

    private MockConnection createAndOpenConnection() throws MessageBrokerException {

        MockMessageBroker mb = new MockMessageBroker(new PathMatchingResourcePatternResolver());
        MockConnectionOptions options = new MockConnectionOptions();

        options.setScenarioResourcePath("classpath:/messageBroker/mock/*.json");

        MockConnection connection = (MockConnection)mb.prepareConnection(options);
        connection.open();

        return connection;
    }

    @Test
    public void createAndOpenConnectionTest() throws MessageBrokerException {
        try(MockConnection c = createAndOpenConnection()) {}
    }

    @Test
    public void closeAndOpenAgainTest() throws MessageBrokerException {
        MockConnection connection = createAndOpenConnection();
        connection.close();
        connection.open();
        connection.close();
    }

    @Test
    public void cannotCloseNotOpenedConnectionTest() throws MessageBrokerException {
        MockConnection connection = createAndOpenConnection();
        connection.close();
        Assertions.assertThrows(MessageBrokerException.class, () -> {connection.close();});
    }

    @Test
    public void mockServiceTaskHandlerTest() throws MessageBrokerException, InterruptedException {

        done = false;

        try(MockConnection connection = createAndOpenConnection()) {

            MockSubscribeRequest rq = new MockSubscribeRequest();
            rq.setTopic("service-task.completion");

            rq.setMessageHandler((msg) -> { done = true; });
            connection.subscribe(rq);

            MessageBrokerPublishRequest pr = new MessageBrokerPublishRequest();
            pr.setSubject("test.s-task");

            JsonObject message = new JsonObject();
            JsonObject context = new JsonObject();
            JsonObject variables = new JsonObject();

            variables.add("variable", new Gson().toJsonTree("value"));

            context.add("variables", variables);
            message.add("context", context);
            message.add("taskExecutionId",  new Gson().toJsonTree("123"));

            pr.setMessage(message.toString());
            connection.publish(pr);

            int waitCounter = 0;

            while(!done || waitCounter > 50){
                Thread.sleep(100);
                waitCounter += 1;
            }

            Assert.assertEquals(true, done);

        }

    }

    @Test
    public void mockUserTaskHandlerTest() throws MessageBrokerException, InterruptedException {

        done = false;

        try(MockConnection connection = createAndOpenConnection()) {

            MockSubscribeRequest rq = new MockSubscribeRequest();
            rq.setTopic("user-task.completion");

            rq.setMessageHandler((msg) -> { done = true; });
            connection.subscribe(rq);

            MessageBrokerPublishRequest pr = new MessageBrokerPublishRequest();
            pr.setSubject("test.u-task");

            JsonObject message = new JsonObject();
            JsonObject context = new JsonObject();
            JsonObject variables = new JsonObject();

            variables.add("variable", new Gson().toJsonTree("value"));

            context.add("variables", variables);
            message.add("context", context);
            message.add("taskId",  new Gson().toJsonTree("123"));

            pr.setMessage(message.toString());
            connection.publish(pr);

            int waitCounter = 0;

            while(!done || waitCounter > 50){
                Thread.sleep(100);
                waitCounter += 1;
            }

            Assert.assertEquals(true, done);

        }

    }

}
