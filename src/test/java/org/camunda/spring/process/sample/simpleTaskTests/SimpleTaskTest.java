package org.camunda.spring.process.sample.simpleTaskTests;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.process.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class SimpleTaskTest extends BaseProcessTest {

    @Test
    public void serviceTaskTest() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/sample/simpleTaskTests/mock-subscribers.json");
        deploy("camunda/spring/process/sample/simpleTaskTests/service-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("service-task");

        HistoricProcessInstance resultPi = processEngine
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        Assert.assertEquals("COMPLETED", resultPi.getState());
    }

    @Test
    public void userTaskTest() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/sample/simpleTaskTests/mock-subscribers.json");
        deploy("camunda/spring/process/sample/simpleTaskTests/user-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        HistoricProcessInstance resultPi = processEngine
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        Assert.assertEquals("COMPLETED", resultPi.getState());
    }

}
