package org.camunda.api.deployment.dto;

import java.util.List;

public class DeploymentRq {

    // folder name from where diagram files are browsed
    // if empty, folder name is taken from application.properties "org.camunda.deployment.source-path"
    public String folder;

    public List<DeploymentItemRq> items;

}
