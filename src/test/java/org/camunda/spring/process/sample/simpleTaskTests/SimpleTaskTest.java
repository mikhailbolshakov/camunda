package org.camunda.spring.process.sample.simpleTaskTests;


import com.google.gson.Gson;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
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

        assertProcessState(pi.getId(), "COMPLETED");

    }

    @Test
    public void userTaskTest() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/sample/simpleTaskTests/mock-subscribers.json");
        deploy("camunda/spring/process/sample/simpleTaskTests/user-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_setVariable_Test()  {

        deploy("camunda/spring/process/sample/simpleTaskTests/script-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        Object var = getVariable(pi.getId(), "var");

        Assert.assertNotNull(var);
        Assert.assertEquals("value", var);

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_setObjectVariableWithSpin_Test()  {

        deploy("camunda/spring/process/sample/simpleTaskTests/script-task.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("user-task-process");

        Object var = getVariable(pi.getId(), "varObj");

        Assert.assertNotNull(var);
        Assert.assertEquals("{\"key1\":\"val1\",\"key2\":\"val2\"}", var.toString());

        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void scriptTask_utilityClass_RunFromScript_Test()  {

        deploy("camunda/spring/process/sample/simpleTaskTests/utility-test.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("test-process");

        Object var = getVariable(pi.getId(), "var");

        Assert.assertNotNull(var);

        assertProcessState(pi.getId(), "COMPLETED");

        assertExecutionPath(pi.getId(), "start -> script -> check -> end1");
    }

}
