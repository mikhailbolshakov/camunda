package org.camunda.spring.bpmn.sample.simpleTaskTests;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class SimpleTaskTest extends BaseProcessTest {

    @Test
    public void serviceTaskTest() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/sample/simpleTaskTests/mock-subscribers.json");
        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/service-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("service-task");

        assertProcessState(pi.getId(), "COMPLETED");

    }

    @Test
    public void userTaskTest() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/sample/simpleTaskTests/mock-subscribers.json");
        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/user-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_setVariable_Test()  {

        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/script-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        Object var = getVariable(pi.getId(), "var");

        Assert.assertNotNull(var);
        Assert.assertEquals("value", var);

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_setObjectVariableWithSpin_Test()  {

        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/script-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        Object var = getVariable(pi.getId(), "varObj");

        Assert.assertNotNull(var);
        Assert.assertEquals("{\"key1\":\"val1\",\"key2\":\"val2\"}", var.toString());

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_utilityClass_RunFromScript_Test()  {

        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/utility-test.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("test-process");

        Object var = getVariable(pi.getId(), "var");

        Assert.assertNotNull(var);

        assertProcessState(pi.getId(), "COMPLETED");

        assertExecutionPath(pi.getId(), "start -> script -> check -> end1");
    }

    @Test
    public void userTask_ExternalTemplate_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/sample/simpleTaskTests/mock-subscribers.json");
        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/userTask-gen-listener.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void throwEvent_StartProcessTemplate_Test() throws MessageBrokerException {

        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/throw-event-start-process.bpmn");
        deployResource("camunda/spring/bpmn/sample/simpleTaskTests/start-message-empty-process.bpmn");

        String piId = startProcess("test-process", new HashMap<>());

        assertProcessState(piId, "COMPLETED");
    }

}
