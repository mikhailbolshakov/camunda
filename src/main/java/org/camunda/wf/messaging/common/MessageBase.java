package org.camunda.wf.messaging.common;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class MessageBase {

    private Header header;
    public Header getHeader() {return header;}
    public void setHeader(Header value) {header = value;}

    private Map<String, Object> variables;
    public Map<String, Object> getVariables() {return variables;}
    public void setVariables(Map<String, Object> value) {variables = value;}

    @Override
    public String toString() { return (new Gson()).toJson(this); }

    public static <T> T fromJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }
}
