package com.ocadotechnology.newrelic.apiclient.model.channels;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

public class IncludeJsonAttachmentDeserializerTest {

    public static class SampleJson {

        @JsonDeserialize(using = IncludeJsonAttachmentDeserializer.class)
        public Boolean sampleField;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserialize_shouldDeserializeToTrue_whenTrueProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : true }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isTrue();
    }

    @Test
    public void deserialize_shouldDeserializeToFalse_whenFalseProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : false }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isFalse();
    }

    @Test
    public void deserialize_shouldDeserializeToTrue_whenTrueStringProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : \"true\" }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isTrue();
    }

    @Test
    public void deserialize_shouldDeserializeToFalse_whenFalseStringProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : \"false\" }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isFalse();
    }

    @Test
    public void deserialize_shouldDeserializeToTrue_when1StringProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : \"1\" }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isTrue();
    }

    @Test
    public void deserialize_shouldDeserializeToFalse_when0StringProvided() throws IOException {

        // given
        String sampleJsonValue = "{ \"sampleField\" : \"0\" }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isFalse();
    }

    @Test
    public void deserialize_shouldDeserializeToNull_whenEmptyStringProvided() throws IOException {
        // given
        String sampleJsonValue = "{ \"sampleField\" : \"\" }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isNull();
    }

    @Test
    public void deserialize_shouldDeserializeToNull_whenNoValueProvided() throws IOException {
        // given
        String sampleJsonValue = "{ }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isNull();
    }

    @Test
    public void deserialize_shouldDeserializeToNull_whenNullProvided() throws IOException {
        // given
        String sampleJsonValue = "{ \"sampleField\" : null }";

        // when
        SampleJson sampleJson = objectMapper.readValue(sampleJsonValue, SampleJson.class);

        //then
        assertThat(sampleJson.sampleField).isNull();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void deserialize_shouldThrowException_unknownValueProvided() throws IOException {
        // given
        String sampleJsonValue = "{ \"sampleField\" : \"some-unknown-value\" }";

        // then
        expectedException.expect(JsonMappingException.class);
        expectedException.expect(hasMessage(containsString("Unsupported field value: some-unknown-value")));

        // when
        objectMapper.readValue(sampleJsonValue, SampleJson.class);
    }
}