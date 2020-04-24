package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.common.libs.messaging.nats.Nats;
import org.camunda.common.wf.message.ExecutionContext;

/*

By its nature Service task isn't async (meaning it's completed after Delegate execution)
But to implement pub/sub approach we need a service task to wait until completion event comes.
So, to achieve this some hack solution suggested by Camunda team is implemented
https://github.com/camunda/camunda-bpm-examples/tree/master/servicetask/service-invocation-asynchronous

 */

public class ServiceTaskDelegate extends AbstractBpmnActivityBehavior {

    private ServiceTaskDelegateImpl delegateImpl;

    public ServiceTaskDelegate() {
        delegateImpl = new ServiceTaskDelegateImpl();
    }

    public void execute(final ActivityExecution execution) throws Exception {
        delegateImpl.execute(execution);
    }

    public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
        leave(execution);
    }

}