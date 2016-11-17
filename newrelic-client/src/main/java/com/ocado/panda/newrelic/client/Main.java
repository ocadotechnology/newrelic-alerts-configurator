package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.api.NewRelicApi;
import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.sync.ApplicationSynchronizer;
import com.ocado.pandateam.newrelic.sync.ChannelSynchronizer;
import com.ocado.pandateam.newrelic.sync.ConditionsSynchronizer;
import com.ocado.pandateam.newrelic.sync.PolicySynchronizer;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ChannelConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.ConditionsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmAppCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ConditionScope;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.DurationTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.OperatorTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.PriorityTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TimeFunctionTerm;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;

import java.util.Collections;

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
            synchronizePolicyConditions(api);
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

    private static void synchronizePolicyConditions(NewRelicApi api) throws NewRelicSyncException {
        ConditionsConfiguration conditionsConfig = ConditionsConfiguration.builder()
            .policyName(POLICY_NAME)
            .conditions(
                Collections.singletonList(
                    ApmAppCondition.builder()
                        .conditionName("Error percentage (High)")
                        .enabled(false)
                        .entities(Collections.singletonList(APPLICATION_NAME))
                        .metric(ApmAppCondition.Metric.APDEX)
                        .conditionScope(ConditionScope.APPLICATION)
                        .terms(
                            Collections.singletonList(
                                TermsConfiguration.builder()
                                    .durationTerm(DurationTerm.DURATION_10)
                                    .operatorTerm(OperatorTerm.ABOVE)
                                    .priorityTerm(PriorityTerm.CRITICAL)
                                    .thresholdTerm(1)
                                    .timeFunctionTerm(TimeFunctionTerm.ALL)
                                    .build()
                            )
                        )
                        .build()
                )
            )
            .build();

        ConditionsSynchronizer synchronizer = new ConditionsSynchronizer(api, conditionsConfig);
        synchronizer.sync();
    }

    private static void synchronizeChannels(NewRelicApi api) throws NewRelicSyncException {
        ChannelConfiguration channelConfig = ChannelConfiguration.builder()
            .policyName(POLICY_NAME)
            .channels(
                Collections.singletonList(
                    EmailChannel.builder()
                        .channelName("Panda team")
                        .emailAddress("core-services-internal-xd@ocado.com")
                        .includeJsonAttachment(false)
                        .build()
                ))
            .build();
        ChannelSynchronizer synchronizer = new ChannelSynchronizer(api, channelConfig);
        synchronizer.sync();
    }
}
