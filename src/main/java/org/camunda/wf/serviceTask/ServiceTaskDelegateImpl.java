package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.common.base.BaseImpl;
import org.camunda.common.libs.messaging.nats.Nats;
import org.camunda.common.wf.message.ExecutionContext;

public class ServiceTaskDelegateImpl extends BaseImpl {

    public void execute(ActivityExecution execution) throws Exception {

        D(String.format("executionId = %s", execution.getId()));

        try {

            // TODO: use factory or builder
            ServiceTaskOutgoingMessage msg = new ServiceTaskOutgoingMessage();

            ExecutionContext ctx = msg.getContext();
            ctx.setProcessDefId(execution.getProcessDefinitionId());
            ctx.setProcessId(execution.getProcessInstanceId());
            ctx.setVariables(execution.getVariables());
            msg.setTaskExecutionId(execution.getId());

            D("Send message: %s", msg.toString());

            // TODO: call through MessageBroker interface
            Nats.publish(execution.getCurrentActivityId(), msg.toString());
        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }

}