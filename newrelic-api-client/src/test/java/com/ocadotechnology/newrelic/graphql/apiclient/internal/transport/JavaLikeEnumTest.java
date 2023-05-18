package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JavaLikeEnumTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSerialize() throws Exception {
        assertThat(serialize("enum", "value")).isEqualTo("{\"enum\":value}");
        assertThat(serialize("enum", "fancy0 @val ^#ue_ ")).isEqualTo("{\"enum\":fancy0value_}");
    }

    @Test
    public void shouldNotSerialize() throws Exception {
        assertThatThrownBy(() -> serialize("enum", null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> serialize("enum", "")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> serialize("enum", "  ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> serialize("enum", "!@#")).isInstanceOf(IllegalArgumentException.class);
    }

    private String serialize(String name, String value) throws Exception {
        return objectMapper.writeValueAsString(ImmutableMap.of(name, new JavaLikeEnum(value)));
    }
}