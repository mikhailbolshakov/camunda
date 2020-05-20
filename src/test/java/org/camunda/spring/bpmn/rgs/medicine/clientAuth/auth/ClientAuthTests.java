package org.camunda.spring.bpmn.rgs.medicine.clientAuth.auth;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
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

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/auth/test-1/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-auth-mk.bpmn");

        String piId = startProcess("rgs.med.client-auth-mk", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> is-auth -> already-auth-end");

    }

    @Test
    public void OPD_NotProvided_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/auth/test-2/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-auth-mk.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientAuth/auth/test-2/opd-mock.bpmn");

        String piId = startProcess("rgs.med.client-auth-mk", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> is-auth -> opd -> is-opd-confirmed -> no-opd-end");

    }

    @Test
    public void Auth_Successfully_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/auth/test-3/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-auth-mk.bpmn");
        deployResource("camunda/spring/bpmn/rgs/medicine/clientAuth/auth/test-3/opd-mock.bpmn");

        String piId = startProcess("rgs.med.client-auth-mk", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> get-client -> is-auth -> opd -> is-opd-confirmed -> reg-data -> code-send -> auth-by-code -> is-resend-code -> end");

    }

}
