package org.camunda.spring.process.setup;


import org.camunda.Application;
import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.service.ProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = {"classpath:/camunda/spring/process/setup/test.properties"})
public abstract class BaseProcessTest {

    @Autowired
    protected ProcessService processService;

    @Autowired
    protected ProcessEngine processEngine;

    @Autowired
    protected MessageBrokerConnection connection;

    protected String startProcess(String processKey, Map<String, Object> variables) {
        StartProcessRq rq = new StartProcessRq();
        rq.processKey = processKey;
        rq.variables = variables;

        return processService.startProcess(rq).processInstanceId;

    }

    protected void setMessageBrokerMockResource(String resourcePath) throws MessageBrokerException {
        if(connection.isOpen())
            connection.close();

        ((MockConnection) connection).registerMockSubscribers(resourcePath);

        connection.open();
    }

    protected void deploy(String classPathResource) {
        processEngine
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource(classPathResource)
                .deploy();
    }

    protected void assertProcessState(String processInstanceId, String expectedState) {

        HistoricProcessInstance resultPi = processEngine
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        Assert.assertEquals(expectedState, resultPi.getState());
    }

    protected String getExecutionPath(String processInstanceId) {
        List<HistoricActivityInstance> haiList = processEngine
                .getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        haiList.sort(
                        new Comparator<HistoricActivityInstance>() {
                            @Override
                            public int compare(HistoricActivityInstance h1, HistoricActivityInstance h2) {

                                long t1 = h1.getStartTime().getTime();
                                long t2 = h2.getStartTime().getTime();

                                if(t1 == t2) {
                                    long e1 = h1.getEndTime().getTime();
                                    long e2 = h2.getEndTime().getTime();

                                    if (e1 == e2) {
                                        if (h1.getActivityType().toUpperCase().contains("STARTEVENT"))
                                            return 1;
                                        if (h1.getActivityType().toUpperCase().contains("ENDEVENT"))
                                            return -1;
                                        return 0;
                                    }

                                    return e1 > e2 ? 1 : -1;

                                }

                                return t1 > t2 ? 1 : -1;
                            }
                        }
                );

        StringBuilder pathSb = new StringBuilder();

        for(int i = 0; i < haiList.size(); i++){
            HistoricActivityInstance hai = haiList.get(i);
            pathSb.append(hai.getActivityId());
            pathSb.append(i < haiList.size() - 1 ? " -> " : "");
        }

        return pathSb.toString();
    }

    protected void assertExecutionPath(String processInstanceId, String expectedPath) {

        String executionPath = getExecutionPath(processInstanceId);

        System.out.println(executionPath);
        Assert.assertEquals(expectedPath, executionPath);

    }

    protected Object getVariable(String processInstanceId, String varName) {

        HistoricVariableInstance hv = processEngine
                .getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(varName)
                .singleResult();

        if (hv == null)
            return null;

        return hv.getValue();
    }

    protected Map<String, Object> getVariables(String processInstanceId) {

        Map<String, Object> variables = new HashMap<>();

        processEngine
                .getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list()
                .forEach((v) -> {variables.put(v.getName(), v.getValue());});

        return variables;
    }

}
