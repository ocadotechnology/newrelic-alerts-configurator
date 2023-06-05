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
        try (InputStream inputStream = inputStream(fileName)) {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }
            return buf.toString("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
