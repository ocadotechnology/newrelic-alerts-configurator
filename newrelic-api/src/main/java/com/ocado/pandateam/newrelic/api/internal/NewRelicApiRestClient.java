package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.model.ObjectList;

import java.util.List;
import java.util.Optional;

public class NewRelicApiRestClient {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicApiRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new Mapper());
    }

    public GetRequest get(String url) {
        return Unirest.get(hostUrl + url).header("X-Api-Key", apiKey);
    }

    public <T> T asObject(Class<? extends T> responseClass, HttpRequest httpRequest) throws NewRelicApiException {
        try {
            return handleErrorResponse(httpRequest, httpRequest.asObject(responseClass)).getBody();
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private <T> HttpResponse<? extends T> handleErrorResponse(HttpRequest httpRequest,
                                                              HttpResponse<? extends T> httpResponse)
            throws NewRelicApiException {
        if (httpResponse.getStatus() / 100 > 3) {
            throw new NewRelicApiException(
                    String.format("NewRelic API returned error code %d: %s; %s %s",
                            httpResponse.getStatus(), httpResponse.getStatusText(),
                            httpRequest.getHttpMethod(), httpRequest.getUrl()));
        }
        return httpResponse;
    }

    public <T> List<T> asList(Class<? extends ObjectList<T>> responseClass, HttpRequest httpRequest)
            throws NewRelicApiException {
        return asObject(responseClass, httpRequest).getList();
    }

    public <T> Optional<T> asSingleObject(Class<? extends ObjectList<T>> responseClass, HttpRequest httpRequest)
            throws NewRelicApiException {
        List<T> list = asList(responseClass, httpRequest);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

}
