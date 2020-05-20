package org.camunda.spring.bpmn.rgs.medicine.mkConsultation.assignMk;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.Test;

public class MkConsultationAssignMKTests extends BaseProcessTest {

    @Test
    public void MkFound_TaskIsInProgress_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-1/mock.json");
        deployFile("bpmn/models/rgs/medicine/mk-consultation/rgs.med.mk-consultation.assign-mk.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.mk-consultation.assign-mk");

        assertProcessState(pi.getId(), "COMPLETED");
        assertExecutionPath(pi.getId(), "start -> init -> find-mk -> if-mk-found -> set-in-progress -> end");
    }

    @Test
    public void MkFound_Escalation_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-2/mock.json");
        deployFile("bpmn/models/rgs/medicine/mk-consultation/rgs.med.mk-consultation.assign-mk.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.mk-consultation.assign-mk");

        // run the job immediately
        Job slaTimeJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(slaTimeJob);
        processEngine.getManagementService().executeJob(slaTimeJob.getId());

        assertExecutionPath(pi.getId(), "start -> init -> find-mk -> if-mk-found -> set-in-progress -> sla-timer -> throw-escalation");
        assertProcessState(pi.getId(), "COMPLETED");
    }

    @Test
    public void MkNotFound_EventMkAvailable_MkFound_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-3/mk-not-found-mock.json");
        deployFile("bpmn/models/rgs/medicine/mk-consultation/rgs.med.mk-consultation.assign-mk.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.mk-consultation.assign-mk");

        assertExecutionPath(pi.getId(), "start -> init -> find-mk -> if-mk-found -> if-mk-has-changed -> set-mkChanged -> wait-mk");
        assertProcessState(pi.getId(), "ACTIVE");

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-3/mk-found-mock.json");

        // send signal "MK available"
        processEngine
                .getRuntimeService()
                .createSignalEvent("rgs.med.mk-consultation.mk-available")
                .send();

        assertExecutionPath(pi.getId(), "start -> init -> find-mk -> if-mk-found -> if-mk-has-changed -> set-mkChanged -> wait-mk -> find-mk -> if-mk-found -> set-in-progress -> end");
        assertProcessState(pi.getId(), "COMPLETED");

     }

    @Test
    public void MkNotFound_ScheduledAnotherCall_MkFound_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-4/mk-not-found-mock.json");
        deployFile("bpmn/models/rgs/medicine/mk-consultation/rgs.med.mk-consultation.assign-mk.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.mk-consultation.assign-mk");

        // send signal "MK available"
        processEngine
                .getRuntimeService()
                .createSignalEvent("rgs.med.mk-consultation.mk-available")
                .send();

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/mkConsultation/assignMk/test-4/mk-found-mock.json");

        // run the appointment job
        Job appointmentJob =  processEngine.getManagementService()
                .createJobQuery()
                .timers()
                .singleResult();

        Assert.assertNotNull(appointmentJob);
        processEngine.getManagementService().executeJob(appointmentJob.getId());

        System.out.println(getExecutionPath(pi.getId()));

        assertProcessState(pi.getId(), "COMPLETED");

    }

}
