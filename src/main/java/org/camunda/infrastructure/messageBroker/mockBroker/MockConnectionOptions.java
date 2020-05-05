package org.camunda.infrastructure.messageBroker.mockBroker;

import org.camunda.repository.messageBroker.MessageBrokerConnectionOptions;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MockConnectionOptions extends MessageBrokerConnectionOptions {

    @Value("${classpath:/messageBroker/mock/*.json}")
    private String scenarioResourcePath;

    public String getScenarioResourcePath() { return scenarioResourcePath; }
    public void setScenarioResourcePath(String value) { scenarioResourcePath = value; }

    @Override
    public void validate() throws MessageBrokerException {
        if(StringUtils.isEmpty(scenarioResourcePath))
            throw new MessageBrokerException("Connection option validation error");
    }

}
