package org.camunda.wf.serviceTask;

import com.google.gson.Gson;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTaskCompletionMessageHandler extends BaseImpl implements MessageHandler {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public void onMessage(Message message) {

        try {

            D("Topic: %s \n Message: %s", message.getTopic(), message.getPayload());

            Gson gson = new Gson();
            ServiceTaskCompleteIncomingMessage completionMessage = gson.fromJson(message.getPayload(), ServiceTaskCompleteIncomingMessage.class);

            processEngine.getRuntimeService().signal(completionMessage.getTaskExecutionId(), completionMessage.getVariables());

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}