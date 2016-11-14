package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.sync.Configuration;
import com.ocado.pandateam.newrelic.sync.NewRelicSyncException;
import com.ocado.pandateam.newrelic.sync.Synchronizer;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws NewRelicApiException, NewRelicSyncException {
        if (args.length == 0 || args[0].length() == 0) {
            throw new IllegalArgumentException("Missing API key");
        } else {
            String apiKey = args[0];
            NewRelicApi api = new NewRelicApi(apiKey);
            Configuration config = Configuration.builder()
                    .applicationName("user_management")
                    .appApdexThreshold(0.5f)
                    .userApdexThreshold(7.0f)
                    .policyName("User Management")
                    .emailChannels(Arrays.asList("core-services-internal-xd@ocado.com"))
                    .build();
            Synchronizer synchronizer = new Synchronizer(api, config);
            synchronizer.sync();
        }
    }
}
