package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;

public class Main {

    public static void main(String[] args) throws NewRelicApiException {
        if (args.length == 0 || args[0].length() == 0) {
            throw new IllegalArgumentException("Missing API key");
        } else {
            String apiKey = args[0];
            NewRelicApi newRelicApi = new NewRelicApi(apiKey);
            System.out.println(newRelicApi.getApplicationByName("user_management"));
        }
    }
}
