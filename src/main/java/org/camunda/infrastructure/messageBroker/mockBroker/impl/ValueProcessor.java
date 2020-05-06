package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.jexl3.*;
import org.camunda.repository.messageBroker.MessageBrokerException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueProcessor {

    private Map<String, Object> jsonContext = null;
    private final Pattern pattern = Pattern.compile("#\\{(.+)\\}");

    public void setContext(Map<String, Object> context) {
        jsonContext = context;
    }

    private JsonObject processJsonObject(JsonObject jsonObject) throws MessageBrokerException {

        JsonObject result = new JsonObject();

        for (Map.Entry<String, JsonElement> attr: jsonObject.entrySet()) {
            result.add(attr.getKey(), processElement(attr.getValue()));
        }

        return result;

    }

    private JsonArray processJsonArray(JsonArray jsonArray) throws MessageBrokerException {

        JsonArray result = new JsonArray();

        for (JsonElement jsonElement : jsonArray) {
            result.add(processElement(jsonElement));
        }

        return result;

    }

    private JsonElement processPrimitive(JsonElement primitive) throws MessageBrokerException {

        if (primitive.getAsJsonPrimitive().isString()) {

            Matcher matcher = pattern.matcher(primitive.getAsString());

            if (matcher.matches()) {
                String jexlExp = matcher.group(1);

                JexlEngine jexl = new JexlBuilder().create();
                JexlExpression e = jexl.createExpression( jexlExp );

                JexlContext jc = new MapContext();
                jc.set("context", jsonContext);

                Object evaluatedObj = e.evaluate(jc);

                if (evaluatedObj == null)
                    throw new MessageBrokerException(String.format("Expression returns null. Expr: %s", jexlExp));

                return new Gson().toJsonTree(evaluatedObj);

            }

        }

        return primitive;
    }

    private JsonElement processElement(JsonElement sourceValue) throws MessageBrokerException {

        if (sourceValue.isJsonObject())
            return processJsonObject(sourceValue.getAsJsonObject());

        if (sourceValue.isJsonArray())
            return processJsonArray(sourceValue.getAsJsonArray());

        if (sourceValue.isJsonPrimitive())
            return processPrimitive(sourceValue);

        if (sourceValue.isJsonNull())
            return sourceValue.getAsJsonNull();

        throw new IllegalArgumentException("Unsupported json element");

    }

    public JsonElement process(JsonElement value) throws MessageBrokerException {
        return processElement(value);
    }


}
