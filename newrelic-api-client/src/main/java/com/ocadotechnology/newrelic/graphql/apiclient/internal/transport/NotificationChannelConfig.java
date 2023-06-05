package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Objects.isNull;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class NotificationChannelConfig {
    List<String> emails;
    Boolean includeJson;
    String userId;
    String teamChannel;
    String baseUrl;
    String customPayloadType;
    List<HttpHeader> customHttpHeaders;
    String customPayloadBody;
    BasicAuth basicAuth;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class BasicAuth {
        String username;
        String password;
    }

    public boolean isEmpty(){
        return isNull(emails) &&
                isNull(includeJson) &&
                isNull(userId) &&
                isNull(teamChannel) &&
                isNull(baseUrl) &&
                isNull(customPayloadType) &&
                isNull(customHttpHeaders) &&
                isNull(customPayloadBody) &&
                isNull(basicAuth);
    }
}
