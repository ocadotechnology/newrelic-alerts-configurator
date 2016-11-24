package com.ocado.panda.newrelic.api.internal;

import com.ocado.panda.newrelic.api.internal.client.NewRelicClient;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import java.util.function.BiFunction;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

abstract class ApiBase {

    protected final NewRelicClient client;

    ApiBase(NewRelicClient client) {
        this.client = client;
    }

    <T> T getPageable(Builder request, Class<T> responseType, BiFunction<T, T, T> accumulator) {
        Response response = request.get();
        T responseBody = response.readEntity(responseType);
        Link next = response.getLink("next");
        return followPageable(next, responseBody, responseType, accumulator);
    }

    private <T> T followPageable(Link next, T responseBody, Class<T> responseType, BiFunction<T, T, T> accumulator) {
        while (next != null) {
            Response nextResponse = client.target(next).request(APPLICATION_JSON_TYPE).get();
            T nextResponseBody = nextResponse.readEntity(responseType);
            responseBody = accumulator.apply(responseBody, nextResponseBody);
            next = nextResponse.getLink("next");
        }
        return responseBody;
    }
}
