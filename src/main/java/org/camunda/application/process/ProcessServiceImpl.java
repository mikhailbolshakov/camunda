package org.camunda.application.process;

import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.dto.StartProcessRs;
import org.camunda.api.process.service.ProcessService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.common.application.base.ApplicationServiceBaseImpl;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Service
public class ProcessServiceImpl extends ApplicationServiceBaseImpl implements ProcessService {

    @Autowired
    private RuntimeService camundaRuntimeService;

    @Override
    public StartProcessRs startProcess(StartProcessRq rq) {

        D("\n=======================================================================================================\n");

        StartProcessRs rs = new StartProcessRs();

        rs.rootBusinessKey = UUID.randomUUID().toString();

        ProcessInstantiationBuilder prBuilder = camundaRuntimeService
                .createProcessInstanceByKey(rq.processKey)
                .setVariables(rq.variables)
                .businessKey(rs.rootBusinessKey)
                .setVariable("rootBusinessKey", rs.rootBusinessKey);

        ProcessInstance pi = prBuilder.execute();

        D("Process started. ProcessInstanceId: %s, rootBusinessKey: %s", pi.getProcessInstanceId(), rs.rootBusinessKey);

        rs.processInstanceId = pi.getProcessInstanceId();

        return rs;
    }

}
