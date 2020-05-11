package org.camunda.web.camunda;

import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.camunda.web.UrlPathConsts;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;


@Component
@ApplicationPath(UrlPathConsts.camundaPath)
public class CamundaRestResourceConfiguration extends CamundaJerseyResourceConfig { }