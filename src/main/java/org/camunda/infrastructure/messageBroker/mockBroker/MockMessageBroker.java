package org.camunda.infrastructure.messageBroker.mockBroker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.repository.messageBroker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockMessageBroker implements MessageBroker {

    private final String MOCK_BROKER_TYPE = "mock";
    private final ResourcePatternResolver resourceLoader;

    private final static Logger logger = LoggerFactory.getLogger(MockMessageBroker.class.getName());

    private final List<MessageBrokerSubscriptionProvider> subscriptionProviders;

    @Autowired
    public MockMessageBroker(ResourcePatternResolver resourceLoader,
                             List<MessageBrokerSubscriptionProvider> subscriptionProviders) {
        this.resourceLoader = resourceLoader;
        this.subscriptionProviders = subscriptionProviders;
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

    @Override
    public String getType() {
        return MOCK_BROKER_TYPE;
    }

    @Override
    public MessageBrokerConnectionOptions createOptions() {
        return ApplicationContextProvider.getContext().getBean(MockConnectionOptions.class);
    }

    @Override
    public MessageBrokerConnection prepareConnection(MessageBrokerConnectionOptions options) throws MessageBrokerException {

        try {

            if(!(options instanceof MockConnectionOptions))
                throw new MessageBrokerException("Unproper options type");

            options.validate();

            MockConnectionOptions op = (MockConnectionOptions)options;

            Resource[] resources = resourceLoader.getResources(op.getScenarioResourcePath());
            Gson jsonParser = new Gson();

            List<JsonObject> scenarios = new ArrayList<>();

            for(Resource r: resources) {

                try (InputStream inputStream = r.getInputStream()) {

                    String content = readFromInputStream(inputStream);
                    JsonObject json = jsonParser.fromJson(content, JsonObject.class);
                    scenarios.add(json);

                }

            }

            return new MockConnection(scenarios, subscriptionProviders);
        }
        catch(Exception e) {
            String errMsg = String.format("[MockMessageBroker] Prepare connection error. Error: %s", e.getMessage());
            logger.error(errMsg, e);
            throw new MessageBrokerException(e, errMsg);
        }

    }
}
