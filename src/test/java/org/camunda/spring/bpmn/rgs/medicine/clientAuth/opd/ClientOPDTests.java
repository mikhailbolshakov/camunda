package org.camunda.spring.bpmn.rgs.medicine.clientAuth.opd;

import com.google.gson.JsonParser;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.camunda.spring.bpmn.setup.BaseProcessTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientOPDTests extends BaseProcessTest {


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
    public void Already_Confirmed_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/opd/test-1/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-opd.bpmn");

        String piId = startProcess("rgs.med.client-opd", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> check-opd -> is-opd-found -> opd-found-end");

    }

    @Test
    public void ClientConfirmation_ByPhone_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/opd/test-2/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-opd.bpmn");

        String piId = startProcess("rgs.med.client-opd", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> check-opd -> is-opd-found -> opd-type -> is-opd-in-platform -> opd-by-phone -> save-opd -> set-confirmed -> end");

        Object opd = getVariable(piId, "opd");
        Assert.assertNotNull(opd);
        Assert.assertEquals(true, JsonParser.parseString(opd.toString()).getAsJsonObject().get("confirmed").getAsBoolean());

    }

    @Test
    public void ClientConfirmation_OnPlatform_Test() throws MessageBrokerException {

        setMessageBrokerMockResource("classpath:/camunda/spring/bpmn/rgs/medicine/clientAuth/opd/test-3/mock.json");
        deployFile("bpmn/models/rgs/medicine/client-authorization/rgs.med.client-opd.bpmn");

        String piId = startProcess("rgs.med.client-opd", initVariables());

        assertProcessState(piId, "COMPLETED");
        assertExecutionPath(piId, "start -> check-opd -> is-opd-found -> opd-type -> is-opd-in-platform -> send-form -> confirm-on-platform -> save-opd -> set-confirmed -> end");

        Object opd = getVariable(piId, "opd");
        Assert.assertNotNull(opd);
        Assert.assertEquals(true, JsonParser.parseString(opd.toString()).getAsJsonObject().get("confirmed").getAsBoolean());

    }

}
