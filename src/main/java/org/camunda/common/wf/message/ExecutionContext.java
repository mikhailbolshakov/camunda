package org.camunda.common.wf.message;

import java.util.Map;

public class ExecutionContext {
    private String processDefId;
    public String getProcessDefId() {return processDefId;}
    public void setProcessDefId(String value) {processDefId = value;}

    private String processId;
    public String getProcessId() {return processId;}
    public void setProcessId(String value) {processId = value;}

    private Map<String, Object> variables;
    public Map<String, Object> getVariables() {return variables;}
    public void setVariables(Map<String, Object> value) {variables = value;}
}
