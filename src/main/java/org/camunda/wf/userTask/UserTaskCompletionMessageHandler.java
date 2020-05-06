package org.camunda.wf.userTask;

import com.google.gson.Gson;
import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.api.task.service.TaskService;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTaskCompletionMessageHandler extends BaseImpl implements MessageHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private org.camunda.bpm.engine.TaskService camundaTaskService;

    @Override
    public void onMessage(Message message) {

        try {

            D("Topic: %s.\n Message: %s", message.getTopic(), message.getPayload());

            Gson gson = new Gson();
            UserTaskCompleteIncomingMessage completionMessage = gson.fromJson(message.getPayload(), UserTaskCompleteIncomingMessage.class);

            TaskCompletionRq rq = new TaskCompletionRq();
            rq.variables = completionMessage.getVariables();

            TaskCompletionRs rs = taskService.complete(completionMessage.getTaskId(), rq);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}