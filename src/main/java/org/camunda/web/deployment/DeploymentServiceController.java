package org.camunda.web.deployment;

import org.camunda.api.deployment.dto.DeploymentRq;
import org.camunda.api.deployment.dto.DeploymentRs;
import org.camunda.api.deployment.service.DeploymentService;
import org.camunda.web.UrlPathConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = UrlPathConsts.rootPath + "/deployment")
public class DeploymentServiceController implements DeploymentService {

    private final DeploymentService deploymentService;

    @Autowired
    public DeploymentServiceController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @Override
    @ResponseBody
    @PostMapping(value = "/deploy")
    public DeploymentRs deploy(@RequestBody DeploymentRq request) throws FileNotFoundException {
        return deploymentService.deploy(request);
    }
}
