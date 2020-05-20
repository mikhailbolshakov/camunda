package org.camunda.spring.bpmn.setup;


import org.camunda.Application;
import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.service.ProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.camunda.repository.messageBroker.MessageBrokerConnection;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = {"classpath:/camunda/spring/bpmn/setup/test.properties"})
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

    // relative path from "classpath:src/test/resource/" without
    protected void deployResource(String classPathResource) {
        processEngine
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource(classPathResource)
                .deploy();
    }

    // relative path from "classpath"
    protected void deployFile(String filename) {

        InputStream inputStream = null;
        try {

            inputStream = new FileInputStream(filename);

            processEngine
                    .getRepositoryService()
                    .createDeployment()
                    .addInputStream(filename, inputStream)
                    .deploy();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
                (h1, h2) -> {
                    long c1 = ((HistoryEvent)h1).getSequenceCounter();
                    long c2 = ((HistoryEvent)h2).getSequenceCounter();
                    return c1 > c2 ? 1 : -1;
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
