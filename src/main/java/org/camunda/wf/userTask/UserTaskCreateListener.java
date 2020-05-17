package org.camunda.wf.userTask;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.common.base.BaseImpl;
import org.springframework.stereotype.Component;

@Component
public class UserTaskCreateListener extends BaseImpl implements TaskListener {

    private final UserTaskCreateListenerImpl delegateImpl;

    public UserTaskCreateListener(UserTaskCreateListenerImpl delegateImpl) {
        this.delegateImpl = delegateImpl;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateImpl.notify(delegateTask);
    }
}