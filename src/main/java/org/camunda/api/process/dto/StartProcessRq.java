package org.camunda.api.process.dto;

import java.util.Map;

public class StartProcessRq {
    public String processKey;
    public Map<String, Object> variables;
}
