package com.ocado.pandateam.newrelic.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DummyApiTest {

    @Test
    public void shouldReturnHelloWorldMessage() throws Exception {
        // given
        DummyApi api = new DummyApi();

        // when
        String message = api.getHelloWorldMessage();

        // then
        assertEquals("Hello, world!", message);
    }

}
