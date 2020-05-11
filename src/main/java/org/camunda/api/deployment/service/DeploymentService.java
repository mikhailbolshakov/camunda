package org.camunda.api.deployment.service;

import org.camunda.api.deployment.dto.DeploymentRq;
import org.camunda.api.deployment.dto.DeploymentRs;

import java.io.FileNotFoundException;

public interface DeploymentService {

    DeploymentRs deploy(DeploymentRq request) throws FileNotFoundException;

}
