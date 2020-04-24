package org.camunda.wf.userTask;

import com.google.gson.Gson;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.api.task.service.TaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.common.base.BaseImpl;
import org.camunda.wf.serviceTask.ServiceTaskCompleteIncomingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class UserTaskCompletionMessageHandler extends BaseImpl implements MessageHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private org.camunda.bpm.engine.TaskService camundaTaskService;

    @Override
    public void onMessage(Message message) throws InterruptedException {

        try {

            String str = new String(message.getData(), StandardCharsets.UTF_8);

            D("Message: %s", str);

            Gson gson = new Gson();
            UserTaskCompleteIncomingMessage completionMessage = gson.fromJson(str, UserTaskCompleteIncomingMessage.class);

            TaskCompletionRq rq = new TaskCompletionRq();
            rq.variables = completionMessage.getVariables();

            TaskCompletionRs rs = taskService.complete(completionMessage.getTaskId(), rq);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}