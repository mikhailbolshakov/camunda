package org.camunda.infrastructure.messageBroker.mockBroker;

public class Message {

    private String topic;
    public String getTopic() {return topic;}
    public void setTopic(String value) {topic = value;}

    private String payload;
    public String getPayload() {return payload;}
    public void setPayload(String value) {payload = value;}

}
