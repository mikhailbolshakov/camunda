package org.camunda.bootstrapper;

import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;


@Component
@ApplicationPath("/camunda/api")
public class RestConfiguration extends CamundaJerseyResourceConfig {

}
