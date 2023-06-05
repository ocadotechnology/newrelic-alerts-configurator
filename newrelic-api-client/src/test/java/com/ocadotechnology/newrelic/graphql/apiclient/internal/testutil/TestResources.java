package com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class TestResources {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static InputStream inputStream(String fileName) {
        return TestResources.class.getResourceAsStream(fileName);
    }

    public static <T> T fromJson(String fileName, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(new InputStreamReader(inputStream(fileName)), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String fromJson(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream(fileName)))) {
            StringBuilder builder = new StringBuilder();
            while (reader.ready()) {
                builder.append(reader.readLine()).append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
