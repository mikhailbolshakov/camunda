package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.common.spring.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

/*

By its nature Service task isn't async (meaning it's completed after Delegate execution)
But to implement pub/sub approach we need a service task to wait until completion event comes.
So, to achieve this some hack solution suggested by Camunda team
See implementation example https://github.com/camunda/camunda-bpm-examples/tree/master/servicetask/service-invocation-asynchronous

 */

@Component
public class ServiceTaskDelegate extends AbstractBpmnActivityBehavior {

    private final ServiceTaskDelegateImpl delegateImpl;

    @Autowired
    public ServiceTaskDelegate(ServiceTaskDelegateImpl delegateImpl) {
        this.delegateImpl = delegateImpl;
    }

    public void execute(final ActivityExecution execution) throws Exception {
        delegateImpl.execute(execution);
    }

    public void signal(ActivityExecution execution, String signalName, Object signalData) {
        leave(execution);
    }

}