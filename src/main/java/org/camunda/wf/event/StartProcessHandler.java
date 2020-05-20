package org.camunda.wf.event;

import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.dto.StartProcessRs;
import org.camunda.api.process.service.ProcessService;
import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.common.base.BaseImpl;
import org.camunda.repository.messageBroker.Message;
import org.camunda.repository.messageBroker.MessageHandler;
import org.camunda.wf.messaging.message.StartProcessMessage;
import org.camunda.wf.messaging.message.TaskCompletionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartProcessHandler extends BaseImpl implements MessageHandler {

    private final ProcessService processService;

    @Autowired
    public StartProcessHandler(ProcessService processService) {
        this.processService = processService;
    }

    @Override
    public void onMessage(Message message) {

        try {

            D("Topic: %s.\n Message: %s", message.getTopic(), message.getPayload());

            StartProcessMessage msg = StartProcessMessage.fromJson(message.getPayload(), StartProcessMessage.class);

            StartProcessRq rq = new StartProcessRq();
            rq.processKey = msg.getStartProcessContext().getProcessKey();
            rq.variables = msg.getVariables();

            StartProcessRs rs = processService.startProcess(rq);

        }
        catch(Exception e) {
            E(e, "Error: %s", e.getMessage());
        }

    }
}
