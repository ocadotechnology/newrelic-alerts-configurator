package com.ocado.pandateam.newrelic.sync;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowException_whenNoApiKey() {
        // given

        // then - exception
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("apiKey");

        // when
        new Synchronizer(null);
    }
}
