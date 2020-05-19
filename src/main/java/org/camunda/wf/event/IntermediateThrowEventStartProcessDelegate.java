package org.camunda.wf.event;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.common.base.BaseImpl;

public class IntermediateThrowEventStartProcessDelegate extends BaseImpl implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {

        IntermediateThrowEvent throwEvent = (IntermediateThrowEvent)execution.getBpmnModelElementInstance();
        MessageEventDefinition eventDef = (MessageEventDefinition)throwEvent.getEventDefinitions().iterator().next();

        String messageName = eventDef.getMessage().getName();

        D("MessageName = %s", messageName);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        runtimeService
                .createProcessInstanceQuery()
                .processInstanceBusinessKey(execution.getProcessInstance().getBusinessKey());

        ProcessInstance procInst = runtimeService.startProcessInstanceByMessage(messageName,
                                                                                execution.getProcessInstance().getBusinessKey(),
                                                                                execution.getVariables());

        D("Process started. ProcInstId = %s", procInst.getProcessInstanceId());
    }
}
