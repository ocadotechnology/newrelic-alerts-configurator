package com.ocado.pandateam.newrelic.api.internal;

import com.mashape.unirest.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class NewRelicResponse<T> {

    private final HttpResponse<T> response;

    NewRelicResponse(HttpResponse<T> response) {
        this.response = response;
    }

    boolean isSuccess() {
        return 200 <= response.getStatus() && response.getStatus() < 300;
    }

    T getBody() {
        return response.getBody();
    }

    void log() {
        log.info("{} {}", response.getStatus(), response.getStatusText());
        if (response.getBody() != null) {
            log.info("{}", response.getBody());
        }
    }
}
