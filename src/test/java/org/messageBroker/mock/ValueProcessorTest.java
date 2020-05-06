package org.messageBroker.mock;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.camunda.infrastructure.messageBroker.mockBroker.impl.ValueProcessor;
import org.camunda.repository.messageBroker.MessageBrokerException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ValueProcessorTest {

    private String readFromResource(Resource resource) throws IOException {

        String content = null;

        try (InputStream inputStream = resource.getInputStream()) {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            content = resultStringBuilder.toString();
        }

        return content;
    }

    private Map<String, Object> loadTestCase(String caseFolderName) throws IOException {
        Map<String, Object> testCase = new HashMap<>();

        boolean requestProvided = false;
        boolean expectedProvided = false;
        boolean contextProvided = false;

        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();

        Resource[] resources = r.getResources(String.format("classpath:/messageBroker/mock/valueProcessorTest/%s/*.json", caseFolderName));

        if (resources.length == 0)
            throw new IllegalArgumentException("Test data isn't found");

        for(Resource resource: resources){

            if (resource.getFilename().equals("request.json")){
                String requestContent = readFromResource(resource);
                testCase.put("request", JsonParser.parseString(requestContent));
                requestProvided = true;
            }

            if (resource.getFilename().equals("expected.json")){
                String expectedContent = readFromResource(resource);
                testCase.put("expected", JsonParser.parseString(expectedContent));
                expectedProvided = true;
            }

            if (resource.getFilename().equals("context.json")){

                String contextContent = readFromResource(resource);

                Map<String, Object> context =  new GsonJsonParser().parseMap(contextContent);
                context.put("random", new Random());

                testCase.put("context", context);
                contextProvided = true;
            }

        }

        if (!requestProvided)
            throw new IllegalArgumentException("Request data isn't provided");

        if (!expectedProvided)
            throw new IllegalArgumentException("Expected data isn't provided");

        if (!contextProvided)
            throw new IllegalArgumentException("Context data isn't provided");

        return testCase;
    }

    @ParameterizedTest
    @ValueSource(strings = {
                            "simple_test",
                            "simple_expression_test",
                            "expression_returns_object_test",
                            "complex_test",
                            "function_expression_test",
                            "substitute_expression_test"
                            })
    public void test(String caseFolderName) throws IOException, MessageBrokerException {
        Map<String, Object> testCase = loadTestCase(caseFolderName);

        ValueProcessor vp = new ValueProcessor();
        vp.setContext((Map<String, Object>)testCase.get("context"));

        String response = vp.process((JsonElement)testCase.get("request")).toString();
        String expected = ((JsonElement)testCase.get("expected")).toString();
        Assert.assertEquals(expected, response);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "expression_error_test"
    })
    public void testWithErrorAssertion(String caseFolderName) throws IOException {
        Map<String, Object> testCase = loadTestCase(caseFolderName);

        ValueProcessor vp = new ValueProcessor();
        vp.setContext((Map<String, Object>)testCase.get("context"));

        Assertions.assertThrows(MessageBrokerException.class, () -> {vp.process((JsonElement)testCase.get("request"));});

    }

}
