package org.camunda.wf.utility;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.HashMap;
import java.util.Map;

public final class ScriptUtility {

    public static Object V(ScriptObjectMirror src) {

        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> item: src.entrySet()) {

            Object value = item.getValue() instanceof ScriptObjectMirror
                    ? V((ScriptObjectMirror)item.getValue())
                    : item.getValue();

            map.put(item.getKey(), value);
        }

        return map;

    }

}
