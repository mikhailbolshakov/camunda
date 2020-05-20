package org.camunda.spring.bpmn.rgs.medicine.clientFeedback.clientTaskFeedback;

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientTaskFeedbackTests extends BaseProcessTest {


    private Map<String, Object> initVariables() {

        Map<String, Object> variables = new HashMap<>();

        Map<String, Object> client = new HashMap<>();
        client.put("userId", "222");
        client.put("phone",  "+79032514432");

        Map<String, Object> assignedMK = new HashMap<>();
        assignedMK.put("userId",  "111");

        variables.put("client", client);
        variables.put("assignedMK", assignedMK);

        return variables;
    }

    @Test
    public void Client_ResolvesTask_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/clientTaskFeedback/test-1/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.client-task-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.sfm.mock.bpmn");

        String piId = startProcess("rgs.med.client-task-feedback", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-os -> sfm -> end");

    }

    @Test
    public void SLA_Timer_Not_RGS_Client_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/clientTaskFeedback/test-2/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.client-task-feedback.bpmn");

        String piId = startProcess("rgs.med.client-task-feedback", initVariables());

        // run the job immediately
        Job nextContactJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(nextContactJob);
        processEngine.getManagementService().executeJob(nextContactJob.getId());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-os -> sla-timer -> is-rgs-client -> delete-os -> end");

    }

    @Test
    public void SLA_Timer_RGS_Client_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/clientTaskFeedback/test-3/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.client-task-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.rgs-task.mock.bpmn");

        String piId = startProcess("rgs.med.client-task-feedback", initVariables());

        // run the job immediately
        Job nextContactJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(nextContactJob);
        processEngine.getManagementService().executeJob(nextContactJob.getId());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-os -> sla-timer -> is-rgs-client -> rgs-task-event -> delete-os -> end");

    }


}
