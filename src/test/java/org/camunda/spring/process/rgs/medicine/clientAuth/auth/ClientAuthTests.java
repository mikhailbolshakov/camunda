package org.camunda.spring.process.rgs.medicine.clientAuth.auth;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.process.setup.BaseProcessTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientAuthTests extends BaseProcessTest {


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
    public void Already_Auth_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/rgs/medicine/clientAuth/auth/test-1/mock.json");
        deploy("camunda/spring/process/rgs/medicine/clientAuth/auth/rgs.med.client-auth-mk.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.client-auth-mk", initVariables());

        assertProcessState(pi.getId(), "COMPLETED");
        assertExecutionPath(pi.getId(), "start -> get-client -> is-auth -> already-auth-end");

    }

    @Test
    public void OPD_NotProvided_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/rgs/medicine/clientAuth/auth/test-2/mock.json");
        deploy("camunda/spring/process/rgs/medicine/clientAuth/auth/rgs.med.client-auth-mk.bpmn");
        deploy("camunda/spring/process/rgs/medicine/clientAuth/auth/test-2/opd-mock.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.client-auth-mk", initVariables());

        assertProcessState(pi.getId(), "COMPLETED");
        assertExecutionPath(pi.getId(), "start -> get-client -> is-auth -> opd -> is-opd-confirmed -> no-opd-end");

    }

    @Test
    public void Auth_Successfully_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/process/rgs/medicine/clientAuth/auth/test-3/mock.json");
        deploy("camunda/spring/process/rgs/medicine/clientAuth/auth/rgs.med.client-auth-mk.bpmn");
        deploy("camunda/spring/process/rgs/medicine/clientAuth/auth/test-3/opd-mock.bpmn");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("rgs.med.client-auth-mk", initVariables());

        getVariables(pi.getId()).forEach((k, v) -> {System.out.println(k + ":" + (v != null ? v.toString() : "null"));});

        assertProcessState(pi.getId(), "COMPLETED");
        assertExecutionPath(pi.getId(), "start -> get-client -> is-auth -> opd -> is-opd-confirmed -> reg-data -> code-send -> auth-by-code -> is-resend-code -> end");

    }

}
