package org.camunda.wf.serviceTask;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;
import org.camunda.wf.messaging.message.TaskCompletionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTaskCompletionHandler extends BaseImpl implements MessageHandler {

    private final ProcessEngine processEngine;

    @Autowired
    public ServiceTaskCompletionHandler(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public void onMessage(Message message) {

        try {

            D("Topic: %s \n Message: %s", message.getTopic(), message.getPayload());

            TaskCompletionMessage msg = TaskCompletionMessage.fromJson(message.getPayload(), TaskCompletionMessage.class);

            processEngine.getRuntimeService().signal(msg.getCompletionContext().getTaskId(), msg.getVariables());

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}