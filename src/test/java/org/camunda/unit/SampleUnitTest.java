package org.camunda.unit;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.wf.serviceTask.ServiceTaskDelegate;
import org.camunda.wf.serviceTask.ServiceTaskDelegateImpl;
import org.junit.*;
import org.mockito.Mock;

@Ignore
public class SampleUnitTest extends AbstractProcessEngineRuleTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources={"camunda/spring/process/sample/simpleTaskTests/service-task.bpmn"})
    public void test(){
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        TaskService taskService = processEngineRule.getTaskService();
        runtimeService.startProcessInstanceByKey("service-task");
        Assert.assertEquals(1, runtimeService.createProcessInstanceQuery().count());
    }
}
