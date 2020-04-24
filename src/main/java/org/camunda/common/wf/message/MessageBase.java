package org.camunda.common.wf.message;

import com.google.gson.Gson;

public abstract class MessageBase {

    private MessageHeader header;
    public MessageHeader getHeader() {return header;}
    public void setHeader(MessageHeader value) {header = value;}

    private ExecutionContext context;
    public ExecutionContext getContext() {return context;}
    public void setContext(ExecutionContext value) {context = value;}

    @Override
    public String toString() { return (new Gson()).toJson(this); }
}
