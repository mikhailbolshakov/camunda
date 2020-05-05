package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import com.google.gson.*;
import org.camunda.infrastructure.messageBroker.mockBroker.Message;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.repository.messageBroker.MessageBrokerPublishRequest;
import org.springframework.boot.json.GsonJsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MockSubscriber implements Subscriber {

    private final JsonObject handler;
    private final String topic;
    private final MockConnection connection;

    public MockSubscriber(MockConnection connection, String topic, JsonObject handler) {
        this.topic = topic;
        this.handler = handler;
        this.connection = connection;
    }

    public void validateHandler() {

    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void Consume(Message message) throws MessageBrokerException, InterruptedException {

        if (handler.has("delay")) {
            Thread.sleep(handler.get("delay").getAsInt());
        }

        JsonArray actions = handler.getAsJsonArray("actions");

        for(JsonElement a : actions) {

            JsonObject action = a.getAsJsonObject();

            String type = action.get("type").getAsString();

            switch(type) {
                case "publish": handlePublishAction(message, action);
                                break;
                default:
                    throw new MessageBrokerException("Unsupported action type");
            }

        }
    }

    private ValueProcessor createValueProcessor(JsonObject sourceMessage) {

        ValueProcessor valueProcessor = new ValueProcessor();

        Map<String, Object> processorCtx = new HashMap<>();
        processorCtx.put("sourceMessage", new GsonJsonParser().parseMap(sourceMessage.toString()));
        processorCtx.put("random", new Random());

        valueProcessor.setContext(processorCtx);

        return valueProcessor;
    }

    private void handlePublishAction(Message msg, JsonObject action) throws MessageBrokerException {

        JsonObject sourceMessage = JsonParser.parseString(msg.getPayload()).getAsJsonObject();

        ValueProcessor valueProcessor = createValueProcessor(sourceMessage);

        MessageBrokerPublishRequest request = new MessageBrokerPublishRequest();

        JsonObject publishMessage = new JsonObject();

        JsonObject messagePrototype = action.get("message").getAsJsonObject();

        for(Map.Entry<String, JsonElement> e : messagePrototype.entrySet()) {

            if(e.getKey().equals("variables")) {

                JsonObject variables = new JsonObject();

                if (action.get("copySourceVariables").getAsBoolean()) {

                    JsonObject sourceVariables = sourceMessage.get("context").getAsJsonObject().get("variables").getAsJsonObject();

                    // copy source variables
                    for(Map.Entry<String, JsonElement> v: sourceVariables.entrySet()) {
                        variables.add(v.getKey(), v.getValue());
                    }

                }

                if(!e.getValue().isJsonObject())
                    throw new MessageBrokerException("Variables must be a JSON object");

                // process variables
                for(Map.Entry<String, JsonElement> v: e.getValue().getAsJsonObject().entrySet()) {
                    variables.add(v.getKey(), valueProcessor.process(v.getValue()));
                }

            }
            else {
                publishMessage.add(e.getKey(), valueProcessor.process(e.getValue()));
            }
        }

        request.setSubject(action.get("topic").getAsString());
        request.setMessage(publishMessage.toString());

        connection.publish(request);

    }

}
