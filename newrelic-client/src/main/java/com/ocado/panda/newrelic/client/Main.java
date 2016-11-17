package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.api.exception.NewRelicApiException;
import com.ocado.pandateam.newrelic.sync.Synchronizer;
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

import java.util.Collection;
import java.util.Collections;

public class Main {
    private static final String APPLICATION_NAME = "user_management";
    private static final String POLICY_NAME = "User Management";

    public static void main(String[] args) throws NewRelicApiException, NewRelicSyncException {
        if (args.length == 0 || args[0].length() == 0) {
            throw new IllegalArgumentException("Missing API key");
        } else {
            String apiKey = args[0];
            Synchronizer synchronizer = Synchronizer.builder()
                .apiKey(apiKey)
                .applicationConfigurations(createApplicationConfigurations())
                .policyConfigurations(createPolicyConfiguraitons())
                .conditionsConfigurations(createConditionsConfigurations())
                .channelConfigurations(createChannelConfigurations())
                .build();
            synchronizer.sync();
        }
    }

    private static Collection<ApplicationConfiguration> createApplicationConfigurations() throws NewRelicSyncException {
        return Collections.singleton(ApplicationConfiguration.builder()
            .applicationName(APPLICATION_NAME)
            .appApdexThreshold(0.5f).endUserApdexThreshold(7.0f)
            .enableRealUserMonitoring(true)
            .build()
        );
    }

    private static Collection<PolicyConfiguration> createPolicyConfiguraitons() throws NewRelicSyncException {
        return Collections.singleton(PolicyConfiguration.builder()
            .policyName(POLICY_NAME)
            .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
            .build()
        );
    }

    private static Collection<ConditionsConfiguration> createConditionsConfigurations() throws NewRelicSyncException {
        return Collections.singleton(ConditionsConfiguration.builder()
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
            .build()
        );
    }

    private static Collection<ChannelConfiguration> createChannelConfigurations() throws NewRelicSyncException {
        return Collections.singleton(ChannelConfiguration.builder()
            .policyName(POLICY_NAME)
            .channels(
                Collections.singletonList(
                    EmailChannel.builder()
                        .channelName("Panda team")
                        .emailAddress("core-services-internal-xd@ocado.com")
                        .includeJsonAttachment(false)
                        .build()
                ))
            .build()
        );
    }
}
