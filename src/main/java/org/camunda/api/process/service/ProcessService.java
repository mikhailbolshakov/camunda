package org.camunda.api.process.service;

import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.dto.StartProcessRs;

public interface ProcessService {

    // start process
    public StartProcessRs startProcess(StartProcessRq rq);

}
