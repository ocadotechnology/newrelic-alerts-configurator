package com.ocadotechnology.newrelic.api.internal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class NewRelicClient {

    private final String hostUrl;

    private final Client client;

    public NewRelicClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        client = ClientBuilder
                .newClient()
                .register(createMapper())
                .register(createInterceptor(apiKey));
    }

    private NewRelicClientInterceptor createInterceptor(String apiKey) {
        return new NewRelicClientInterceptor(apiKey);
    }

    private JacksonJsonProvider createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setSerializationInclusion(NON_NULL);
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        return new JacksonJaxbJsonProvider(mapper, DEFAULT_ANNOTATIONS);
    }

    public WebTarget target(String path) {
        return client.target(hostUrl).path(path);
    }

    public WebTarget target(Link link) {
        return client.target(link);
    }
}
