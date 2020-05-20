package org.camunda.spring.bpmn.rgs.medicine.clientFeedback.mkGatherFeedback;

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MkGatherFeedbackTests extends BaseProcessTest {


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
    public void Client_GivesFeedbackOnline_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/mkGatherFeedback/test-1/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.mk-gather-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.sfm.mock.bpmn");

        String piId = startProcess("rgs.med.mk-gather-feedback", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-contact -> is-contact -> client-os -> is-os-provided -> sfm -> close-contact -> end");

    }

    @Test
    public void Feedback_AfterSecondContact_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/mkGatherFeedback/test-2/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.mk-gather-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.sfm.mock.bpmn");

        String piId = startProcess("rgs.med.mk-gather-feedback", initVariables());

        // run the job immediately
        Job nextContactJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(nextContactJob);
        processEngine.getManagementService().executeJob(nextContactJob.getId());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-contact -> is-contact -> set-next-contact -> next-contact-timer -> client-contact -> is-contact -> client-os -> is-os-provided -> sfm -> close-contact -> end");

    }

    @Test
    public void Client_MoveContact_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/mkGatherFeedback/test-3/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.mk-gather-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.sfm.mock.bpmn");

        String piId = startProcess("rgs.med.mk-gather-feedback", initVariables());

        // run the job immediately
        Job nextContactJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(nextContactJob);
        processEngine.getManagementService().executeJob(nextContactJob.getId());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-contact -> is-contact -> client-os -> is-os-provided -> is-next-contact-agreed -> set-client-next-contact -> next-contact-timer -> client-contact -> is-contact -> client-os -> is-os-provided -> sfm -> close-contact -> end");

    }

    @Test
    public void CreateClientTask_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/mkGatherFeedback/test-4/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.mk-gather-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.client-task.mock.bpmn");

        String piId = startProcess("rgs.med.mk-gather-feedback", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-contact -> is-contact -> client-os -> is-os-provided -> is-next-contact-agreed -> is-client-task -> client-task-event -> close-contact -> end");

    }

    @Test
    public void CreateRgsTask_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientFeedback/mkGatherFeedback/test-5/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-feedback/rgs.med.mk-gather-feedback.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientFeedback/rgs.med.feedback.rgs-task.mock.bpmn");

        String piId = startProcess("rgs.med.mk-gather-feedback", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> client-contact -> is-contact -> client-os -> is-os-provided -> is-next-contact-agreed -> is-client-task -> is-rgs-client -> rgs-task-event -> close-contact -> end");

    }

}
