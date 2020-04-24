package org.camunda.wf.event;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.common.base.BaseImpl;

public class IntermediateThrowEventToCatcherDelegate extends BaseImpl implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {

        IntermediateThrowEvent throwEvent = (IntermediateThrowEvent)execution.getBpmnModelElementInstance();
        MessageEventDefinition eventDef = (MessageEventDefinition)throwEvent.getEventDefinitions().iterator().next();

        String messageName = eventDef.getMessage().getName();

        D("MessageName = %s", messageName);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        MessageCorrelationResult cr = runtimeService
                .createMessageCorrelation(messageName)
                .processInstanceVariableEquals("rootBusinessKey", execution.getVariable("rootBusinessKey"))
                .setVariables(execution.getVariables())
                .correlateWithResult();

        D("Run execution. id: %s", cr.getExecution().getId());

    }
}
