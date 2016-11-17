package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.sync.ApplicationSynchronizer;
import com.ocado.pandateam.newrelic.sync.ChannelSynchronizer;
import com.ocado.pandateam.newrelic.sync.PolicySynchronizer;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.Arrays;

public class Main {
    private static final String APPLICATION_NAME = "user_management";
    private static final String POLICY_NAME = "User Management";

    public static void main(String[] args) throws NewRelicApiException, NewRelicSyncException {
        if (args.length == 0 || args[0].length() == 0) {
            throw new IllegalArgumentException("Missing API key");
        } else {
            String apiKey = args[0];
            NewRelicApi api = new NewRelicApi(apiKey);
            synchronizeApplication(api);
            synchronizePolicy(api);
            synchronizeChannels(api);
        }
    }

    private static void synchronizeApplication(NewRelicApi api) throws NewRelicSyncException {
        ApplicationConfiguration applicationConfig = ApplicationConfiguration.builder()
                .applicationName(APPLICATION_NAME)
                .appApdexThreshold(0.5f).endUserApdexThreshold(7.0f)
                .enableRealUserMonitoring(true)
                .build();
        ApplicationSynchronizer synchronizer = new ApplicationSynchronizer(api, applicationConfig);
        synchronizer.sync();
    }

    private static void synchronizePolicy(NewRelicApi api) throws NewRelicSyncException {
        PolicyConfiguration policyConfig = PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .build();

        PolicySynchronizer synchronizer = new PolicySynchronizer(api, policyConfig);
        synchronizer.sync();
    }

    private static void synchronizeChannels(NewRelicApi api) throws NewRelicSyncException {
        ChannelConfiguration channelConfig = ChannelConfiguration.builder()
                .policyName(POLICY_NAME)
                .channels(
                        Arrays.asList(
                                EmailChannel.builder()
                                        .channelName("Panda team")
                                        .emailAddress("core-services-internal-xd@ocado.com")
                                        .includeJsonAttachment(true)
                                        .build()
                        ))
                .build();
        ChannelSynchronizer synchronizer = new ChannelSynchronizer(api, channelConfig);
        synchronizer.sync();
    }
}
