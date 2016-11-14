package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiHttpException;

public class NewRelicRestClient {

    private final String hostUrl;
    private final String apiKey;

    public NewRelicRestClient(String hostUrl, String apiKey) {
        this.hostUrl = hostUrl;
        this.apiKey = apiKey;
        Unirest.setObjectMapper(new NewRelicRequestMapper());
    }

    /*public NewRelicGetRequest get(String url) {
        return new NewRelicGetRequest(Unirest.get(hostUrl + url).header("X-Api-Key", apiKey));
    }

    public NewRelicRequestWithBody post(String url) {
        return new NewRelicRequestWithBody(Unirest.post(hostUrl + url).header("X-Api-Key", apiKey));
    }

    public NewRelicRequestWithBody put(String url) {
        return new NewRelicRequestWithBody(Unirest.put(hostUrl + url).header("X-Api-Key", apiKey));
    }*/

    public GetRequest get(String url) {
        return Unirest
                .get(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json");
    }

    public HttpRequestWithBody put(String url) {
        return Unirest
                .put(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json")
                .header("Content-Type", "application/json");
    }

    public HttpRequestWithBody post(String url) {
        return Unirest
                .post(hostUrl + url)
                .header("X-Api-Key", apiKey)
                .header("accept", "application/json")
                .header("Content-Type", "application/json");
    }

    public <T> T asObject(BaseRequest request, Class<T> responseType) throws NewRelicApiException {
        try {
            HttpResponse<T> response = request.asObject(responseType);
            return handleResponse(request, response);
        } catch (UnirestException e) {
            throw new NewRelicApiException(e);
        }
    }

    private <T> T handleResponse(BaseRequest request, HttpResponse<T> response) throws NewRelicApiHttpException {
        if (200 <= response.getStatus() && response.getStatus() < 300) {
            return response.getBody();
        } else {
            throw new NewRelicApiHttpException(request, response);
        }
    }
}
