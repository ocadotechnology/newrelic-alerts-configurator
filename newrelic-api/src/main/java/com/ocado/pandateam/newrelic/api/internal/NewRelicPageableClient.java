package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.api.internal.pagination.PageableRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class NewRelicPageableClient {

    private NewRelicRestClient client;

    public NewRelicPageableClient(NewRelicRestClient client) {
        this.client = client;
    }

    public PageableRequest getPageable(String url) {
        return new PageableRequest(get(url));
    }

    public <T> T asPageable(PageableRequest request, Class<T> responseType, Function<List<T>, T> mapper) {
        List<T> responseList = new LinkedList<>();
        while (request.hasNext()) {
            T response = asObject(request, responseType);
            responseList.add(response);
        }
        return mapper.apply(responseList);
    }

    public GetRequest get(String url) {
        return client.get(url);
    }

    public HttpRequestWithBody put(String url) {
        return client.put(url);
    }

    public HttpRequestWithBody put(String url, String contentType) {
        return client.put(url, contentType);
    }

    public HttpRequestWithBody post(String url) {
        return client.post(url);
    }

    public HttpRequestWithBody post(String url, String contentType) {
        return client.post(url, contentType);
    }

    public HttpRequest delete(String url) {
        return client.delete(url);
    }

    public HttpRequest delete(String url, String contentType) {
        return client.delete(url, contentType);
    }

    public <T> T asObject(BaseRequest request, Class<T> responseType) throws NewRelicApiException {
        return client.asObject(request, responseType);
    }
}
