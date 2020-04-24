package org.camunda.common.wf.message;

import java.util.UUID;

public class MessageHeader {

    private String messageId;
    public String getMessageId() {return messageId;}
    public void setMessageId(String value) {messageId = value;}

    private String schemaVersion;
    public String getSchemaVersion() {return schemaVersion;}
    public void setSchemaVersion(String value) {schemaVersion = value;}

    public MessageHeader() {
        messageId = UUID.randomUUID().toString();
        schemaVersion = "v1";
    }
}
