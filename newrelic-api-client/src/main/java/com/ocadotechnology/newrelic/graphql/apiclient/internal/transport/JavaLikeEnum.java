package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Value;

import java.io.IOException;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.util.Preconditions.checkArgument;

@JsonSerialize(using = JavaLikeEnumSerializer.class)
@Value
public class JavaLikeEnum {
    String value;

    public JavaLikeEnum(String value) {
        checkArgument(value != null, "Value cannot be null");

        String normalizedValue = value.replaceAll("[^A-Za-z0-9_]", "");
        checkArgument(!normalizedValue.isEmpty(), "Value cannot be empty");

        this.value = normalizedValue;
    }
}

class JavaLikeEnumSerializer extends StdSerializer<JavaLikeEnum> {

    public JavaLikeEnumSerializer() {
        this(JavaLikeEnum.class);
    }

    protected JavaLikeEnumSerializer(Class<JavaLikeEnum> t) {
        super(t);
    }

    @Override
    public void serialize(JavaLikeEnum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeRawValue(value.getValue());
    }
}
