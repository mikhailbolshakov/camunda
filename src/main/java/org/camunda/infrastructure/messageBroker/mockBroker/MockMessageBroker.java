package org.camunda.infrastructure.messageBroker.mockBroker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MessageProcessor;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MessageProcessorMainThreadImpl;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.MessageProcessorQueueImpl;
import org.camunda.repository.messageBroker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    private MessageProcessor createMessageProcessor(MockConnectionOptions options) {

        if(options.getQueueProcessing()) {
            return new MessageProcessorQueueImpl();
        }
        else {
            return new MessageProcessorMainThreadImpl();
        }

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

            MockConnection connection = new MockConnection(createMessageProcessor(op), resourceLoader, subscriptionProviders);

            if(!StringUtils.isEmpty(op.getScenarioResourcePath()))
                connection.registerMockSubscribers(op.getScenarioResourcePath());

            return connection;
        }
        catch(Exception e) {
            String errMsg = String.format("[MockMessageBroker] Prepare connection error. Error: %s", e.getMessage());
            logger.error(errMsg, e);
            throw new MessageBrokerException(e, errMsg);
        }

    }
}
