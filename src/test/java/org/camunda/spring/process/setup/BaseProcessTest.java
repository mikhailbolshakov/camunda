package org.camunda.spring.process.setup;


import org.camunda.Application;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = {"classpath:/camunda/spring/process/setup/test.properties"})
public abstract class BaseProcessTest {

    @Autowired
    protected ProcessEngine processEngine;

    @Autowired
    protected MessageBrokerConnection connection;

    protected void setMessageBrokerMockResource(String resourcePath) throws MessageBrokerException {
        if(connection.isOpen())
            connection.close();

        ((MockConnection) connection).registerMockSubscribers(resourcePath);

        connection.open();
    }

    protected void deploy(String classPathResource) {
        processEngine
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource(classPathResource)
                .deploy();
    }
}
