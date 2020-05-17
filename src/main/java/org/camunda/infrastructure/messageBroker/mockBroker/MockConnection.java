package org.camunda.infrastructure.messageBroker.mockBroker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.*;
import org.camunda.repository.messageBroker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class MockConnection implements MessageBrokerConnection, AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private List<JsonObject> rules = new ArrayList<>();

    private final MessageProcessor processor;
    private final ResourcePatternResolver resourceLoader;

    private final List<MessageBrokerSubscriptionProvider> subscriptionProviders;

    private boolean isOpen = false;

    public MockConnection(MessageProcessor messageProcessor,
                          ResourcePatternResolver resourceLoader,
                          List<MessageBrokerSubscriptionProvider> subscriptionProviders) throws MessageBrokerException {

        this.processor = messageProcessor;
        this.subscriptionProviders = subscriptionProviders;
        this.resourceLoader = resourceLoader;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void registerMockSubscribers(String resourcePath) throws MessageBrokerException {

        logger.debug(String.format("Read subscribers from resource %s", resourcePath));

        try {
            List<JsonObject> scenarios = new ArrayList<>();

            Resource[] resources = resourceLoader.getResources(resourcePath);

            Gson jsonParser = new Gson();

            for(Resource r: resources) {

                try (InputStream inputStream = r.getInputStream()) {

                    String content = readFromInputStream(inputStream);
                    JsonObject json = jsonParser.fromJson(content, JsonObject.class);
                    scenarios.add(json);

                }

            }

            registerMockSubscribers(scenarios);

        }
        catch(Exception e){
            catchError(e, "Subscribers resource loading error.", true);
        }

    }

    public void registerMockSubscribers(List<JsonObject> rules) throws MessageBrokerException {

        logger.debug(String.format("Mock subscribers registration. Found %d", rules.size()));

        try {

            if(isOpen)
                throw new MessageBrokerException("Subscriber registration isn't allowed on open connection");

            this.rules.clear();
            this.rules.addAll(rules);
            processor.removeSubscribers();

            for(JsonObject rule: rules) {

                for (JsonElement s : rule.getAsJsonArray("subscribers")) {

                    if(!s.isJsonObject())
                        throw new MessageBrokerException("Rule parsing error");

                    JsonObject subscriber = s.getAsJsonObject();

                    String topic = subscriber.get("topic").getAsString();
                    JsonObject handler = subscriber.get("handler").getAsJsonObject();

                    MockSubscriber ms = new MockSubscriber(this, topic, handler);

                    ms.validateHandler();

                    processor.addSubscriber(ms);

                    logger.debug(String.format("[MockMessageBroker] %s - Mock subscriber registered", topic));

                }

            }

        }
        catch(Exception e){
            catchError(e, "Subscribers registration error.", true);
        }

    }

    private void catchError(Throwable e, String message, boolean rethrow) throws MessageBrokerException{
        String errMsg = String.format("[MockMessageBroker] %s.\n Error: %s", message, e.getMessage());
        logger.error(errMsg, e);
        if (rethrow)
            throw new MessageBrokerException(e, errMsg);
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void open() throws MessageBrokerException {

        try {

            checkConnectionState(false);

            processor.start();

            isOpen = true;

            // set subscription if a providers specified
            if (subscriptionProviders != null)
                for(MessageBrokerSubscriptionProvider p: subscriptionProviders){
                    p.subscribe(this);
                }

            logger.debug("[MockMessageBroker] Connection opened");

        }
        catch (Exception e) {
            catchError(e, "Connection error.", true);
        }

    }

    protected void checkConnectionState(boolean open) throws MessageBrokerException {
        if (open != isOpen())
            throw new MessageBrokerException("Connection status cannot be changed");
    }

    @Override
    public void close() throws MessageBrokerException {
        try {

            if (!isOpen())
                throw new MessageBrokerException("Can't close not active connection.");

            processor.stop();

            isOpen = false;

            logger.debug("[MockMessageBroker] Connection closed");

        }
        catch(Exception e) {
            catchError(e, "Close connection error.", true);
        }
    }

    @Override
    public void publish(MessageBrokerPublishRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            Message msg = new Message();
            msg.setTopic(request.getSubject());
            msg.setPayload(request.getMessage());

            processor.process(msg);

            logger.debug(String.format("[MockMessageBroker] Message published.\n Subject: %s.\n Message: %s", request.getSubject(), request.getMessage()));

        }
        catch (Exception e) {
            catchError(e, "Publishing error.", true);
        }
    }

    @Override
    public void subscribe(MessageBrokerSubscribeRequest request) throws MessageBrokerException {

        try {

            checkConnectionState(true);

            request.validate();

            Subscriber s = new HandlerSubscriber(request.getTopic(), request.getMessageHandler());
            processor.addSubscriber(s);

            logger.debug(String.format("[MockMessageBroker] Subscriber registered. Subject: %s", request.getTopic()));

        } catch (Exception e) {
            catchError(e, "Subscriber registration error.", true);
        }

    }

    @Override
    public List<MessageBrokerSubscriptionProvider> getSubscriptionProviders() {
        return subscriptionProviders;
    }

}
