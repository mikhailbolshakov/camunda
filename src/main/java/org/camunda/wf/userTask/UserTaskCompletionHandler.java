package org.camunda.wf.userTask;

import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.api.task.service.TaskService;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;
import org.camunda.wf.messaging.message.TaskCompletionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTaskCompletionHandler extends BaseImpl implements MessageHandler {

    private final TaskService taskService;

    @Autowired
    public UserTaskCompletionHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void onMessage(Message message) {

        try {

            D("Topic: %s.\n Message: %s", message.getTopic(), message.getPayload());

            TaskCompletionMessage msg = TaskCompletionMessage.fromJson(message.getPayload(), TaskCompletionMessage.class);

            TaskCompletionRq rq = new TaskCompletionRq();
            rq.variables = msg.getVariables();

            TaskCompletionRs rs = taskService.complete(msg.getCompletionContext().getTaskId(), rq);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}