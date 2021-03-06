package org.camunda.application.process;

import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.dto.StartProcessRs;
import org.camunda.api.process.service.ProcessService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.common.application.base.ApplicationServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProcessServiceImpl extends ApplicationServiceBaseImpl implements ProcessService {

    private final RuntimeService camundaRuntimeService;

    @Autowired
    public ProcessServiceImpl(RuntimeService camundaRuntimeService) {
        this.camundaRuntimeService = camundaRuntimeService;
    }

    @Override
    public StartProcessRs startProcess(StartProcessRq rq) {

        D("\n=======================================================================================================\n");

        try {
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
        catch(Exception e) {
            E(e, "Start process error. Key: %s ", rq.processKey);
            throw e;
        }
    }

}
