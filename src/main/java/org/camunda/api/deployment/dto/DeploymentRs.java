package org.camunda.api.deployment.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeploymentRs {

    public DeploymentRs() {
        items = new ArrayList<>();
    }

    public List<DeploymentItemRs> items;
}
