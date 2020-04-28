package org.camunda.wf.serviceTask;

import com.google.gson.Gson;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.common.base.BaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ServiceTaskCompletionMessageHandler extends BaseImpl implements MessageHandler {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public void onMessage(Message message) throws InterruptedException {

        try {

            String str = new String(message.getData(), StandardCharsets.UTF_8);

            D("Message: %s", str);

            Gson gson = new Gson();
            ServiceTaskCompleteIncomingMessage completionMessage = gson.fromJson(str, ServiceTaskCompleteIncomingMessage.class);

            processEngine.getRuntimeService().signal(completionMessage.getTaskExecutionId(), completionMessage.getVariables());

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}