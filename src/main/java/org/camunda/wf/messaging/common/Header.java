package org.camunda.wf.messaging.common;

import java.util.Date;
import java.util.UUID;

public class Header {

    private String messageId;
    public String getMessageId() {return messageId;}
    public void setMessageId(String value) {messageId = value;}

    private String schemaVersion;
    public String getSchemaVersion() {return schemaVersion;}
    public void setSchemaVersion(String value) {schemaVersion = value;}

    private Date timestamp;
    public Date getTimestamp() {return timestamp;}
    public void setTimestamp(Date value) {timestamp = value;}

}
