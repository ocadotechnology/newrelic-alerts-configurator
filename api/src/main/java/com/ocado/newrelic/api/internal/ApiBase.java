package com.ocado.newrelic.api.internal;

import com.ocado.newrelic.api.internal.client.NewRelicClient;
import com.ocado.newrelic.api.model.ObjectList;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

abstract class ApiBase {

    protected final NewRelicClient client;

    ApiBase(NewRelicClient client) {
        this.client = client;
    }

    <T, LT extends ObjectList<T, LT>> LT getPageable(Builder request, Class<LT> responseType) {
        Response response = request.get();
        LT responseBody = response.readEntity(responseType);
        Link next = response.getLink("next");
        return followPageable(next, responseBody, responseType);
    }

    private <T, LT extends ObjectList<T, LT>> LT followPageable(Link next, LT responseBody, Class<LT> responseType) {
        while (next != null) {
            Response nextResponse = client.target(next).request(APPLICATION_JSON_TYPE).get();
            LT nextResponseBody = nextResponse.readEntity(responseType);
            responseBody = responseBody.merge(nextResponseBody);
            next = nextResponse.getLink("next");
        }
        return responseBody;
    }
}
