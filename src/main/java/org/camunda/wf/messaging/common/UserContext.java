package org.camunda.wf.messaging.common;

public class UserContext {

    private String formId;
    public String getFormId() {return formId;}
    public void setFormId(String value) { formId = value;}

    private String assignedUserId;
    public String getAssignedUserId() {return assignedUserId;}
    public void setAssignedUserId(String value) { assignedUserId = value;}

}
