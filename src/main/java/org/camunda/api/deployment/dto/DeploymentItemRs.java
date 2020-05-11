package org.camunda.api.deployment.dto;

import java.util.Date;

public class DeploymentItemRs {

    // taken from the request
    public String deploymentName;

    // internal ID of deployment
    public String deploymentId;

    public Date deploymentTime;

    // if deployment failed, error is returned
    public String error;
}
