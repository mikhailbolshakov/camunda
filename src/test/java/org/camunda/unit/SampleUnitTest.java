package org.camunda.unit;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.*;

@Ignore
public class SampleUnitTest extends AbstractProcessEngineRuleTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources={"camunda/spring/bpmn/sample/simpleTaskTests/service-task.bpmn"})
    public void test(){
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        TaskService taskService = processEngineRule.getTaskService();
        runtimeService.startProcessInstanceByKey("service-task");
        Assert.assertEquals(1, runtimeService.createProcessInstanceQuery().count());
    }
}
