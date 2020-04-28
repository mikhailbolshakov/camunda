package org.camunda.web.process;

import org.camunda.api.process.dto.StartProcessRq;
import org.camunda.api.process.dto.StartProcessRs;
import org.camunda.api.process.service.ProcessService;
import org.camunda.web.UrlPathConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UrlPathConsts.rootPath + "/process")
public class ProcessServiceController implements ProcessService {

    @Autowired
    private ProcessService processservice;

    @Override
    @ResponseBody
    @PostMapping(value = "/instance")
    public StartProcessRs startProcess(@RequestBody StartProcessRq rq) {
        return processservice.startProcess(rq);
    }
}
