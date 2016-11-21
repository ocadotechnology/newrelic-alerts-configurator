package com.ocado.pandateam.newrelic.api.internal.pagination;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import java.util.Map;

public class PageableRequest extends GetRequest {

    public PageableRequest(GetRequest request) {
        super(request.getHttpMethod(), request.getUrl());
        request.getHeaders().forEach((name, values) -> values.forEach(value -> header(name, value)));
    }

    @Override
    public PageableRequest routeParam(String name, String value) {
        super.routeParam(name, value);
        return this;
    }

    @Override
    public PageableRequest header(String name, String value) {
        super.header(name, value);
        return this;
    }

    @Override
    public PageableRequest headers(Map<String, String> headers) {
        super.headers(headers);
        return this;
    }

    @Override
    public PageableRequest basicAuth(String username, String password) {
        super.basicAuth(username, password);
        return this;
    }

    public boolean hasNext() {
        return url != null;
    }

    @Override
    public <T> HttpResponse<T> asObject(Class<? extends T> responseClass) throws UnirestException {
        HttpResponse<T> response = super.asObject(responseClass);
        PageableResponse pageableResponse = new PageableResponse(response);
        url = pageableResponse.getNext();
        return response;
    }
}
