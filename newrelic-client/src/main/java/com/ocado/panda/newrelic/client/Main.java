package com.ocado.panda.newrelic.client;

import com.ocado.pandateam.newrelic.sync.Synchronizer;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.PolicyConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.EmailChannel;
import com.ocado.pandateam.newrelic.sync.configuration.channel.SlackChannel;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmAppCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ApmKeyTransactionCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.Condition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.ExternalServiceCondition;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.DurationTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.OperatorTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.PriorityTerm;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import com.ocado.pandateam.newrelic.sync.configuration.condition.terms.TimeFunctionTerm;

import java.util.Collection;
import java.util.Collections;

public class Main {
    private static final String APPLICATION_NAME = "user_management";
    private static final String POLICY_NAME = "User Management";

    public static void main(String[] args) {
        if (args.length == 0 || args[0].length() == 0) {
            throw new IllegalArgumentException("Missing API key");
        } else {
            String apiKey = args[0];

            Synchronizer synchronizer = new Synchronizer(apiKey);
            synchronizer.setApplicationConfigurations(createApplicationConfigurations());
            synchronizer.setPolicyConfigurations(createPolicyConfigurations());

            synchronizer.sync();
        }
    }

    private static Collection<ApplicationConfiguration> createApplicationConfigurations() {
        return Collections.singleton(
            ApplicationConfiguration.builder()
                .applicationName(APPLICATION_NAME)
                .appApdexThreshold(0.5f)
                .endUserApdexThreshold(7.0f)
                .enableRealUserMonitoring(true)
                .build()
        );
    }

    private static Collection<PolicyConfiguration> createPolicyConfigurations() {
        return Collections.singleton(
            PolicyConfiguration.builder()
                .policyName(POLICY_NAME)
                .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
                .condition(apdexConditionConfiguration())
                .condition(keyTransactionConditionConfiguration())
                .externalServiceCondition(kmsAverageResponseConditionConfiguration())
                .externalServiceCondition(kmsMaximumResponseConditionConfiguration())
                .channel(emailChannelConfiguration())
                .build()
        );
    }

    private static Condition apdexConditionConfiguration() {
        return ApmAppCondition.builder()
            .conditionName("Apdex (Low)")
            .enabled(true)
            .entity(APPLICATION_NAME)
            .metric(ApmAppCondition.Metric.APDEX)
            .conditionScope(ApmAppCondition.ConditionScope.APPLICATION)
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.BELOW)
                .priorityTerm(PriorityTerm.CRITICAL)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(0.7f)
                .build()
            )
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.BELOW)
                .priorityTerm(PriorityTerm.WARNING)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(0.85f)
                .build()
            )
            .build();
    }

    private static Condition keyTransactionConditionConfiguration() {
        return ApmKeyTransactionCondition.builder()
            .conditionName("Key transactions error count (High)")
            .enabled(true)
            .entity("user_management /v2/domains/{domainId}/authenticate (POST)")
            .entity("user_management /v2/domains/{domainId}/userdetails/{login:.+} (GET)")
            .metric(ApmKeyTransactionCondition.Metric.ERROR_COUNT)
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(5.0f)
                .build()
            )
            .build();
    }

    private static ExternalServiceCondition kmsAverageResponseConditionConfiguration() {
        return ApmExternalServiceCondition.builder()
            .conditionName("Kms average response time")
            .enabled(true)
            .entity(APPLICATION_NAME)
            .externalServiceUrl("kms.eu-west-1.amazonaws.com")
            .metric(ApmExternalServiceCondition.Metric.RESPONSE_TIME_AVERAGE)
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(5.0f)
                .build()
            )
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.WARNING)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(2.5f)
                .build()
            )
            .build();
    }

    private static ExternalServiceCondition kmsMaximumResponseConditionConfiguration() {
        return ApmExternalServiceCondition.builder()
            .conditionName("Kms maximum response time")
            .enabled(true)
            .entity(APPLICATION_NAME)
            .externalServiceUrl("kms.eu-west-1.amazonaws.com")
            .metric(ApmExternalServiceCondition.Metric.RESPONSE_TIME_MAXIMUM)
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.CRITICAL)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(5.0f)
                .build()
            )
            .term(TermsConfiguration.builder()
                .durationTerm(DurationTerm.DURATION_5)
                .operatorTerm(OperatorTerm.ABOVE)
                .priorityTerm(PriorityTerm.WARNING)
                .timeFunctionTerm(TimeFunctionTerm.ALL)
                .thresholdTerm(3.0f)
                .build()
            )
            .build();
    }

    private static Channel emailChannelConfiguration() {
        return EmailChannel.builder()
            .channelName("Panda team - email")
            .emailAddress("core-services-internal-xd@ocado.com")
            .includeJsonAttachment(false)
            .build();
    }

    private static Channel slackChannelConfiguration() {
        // TODO
        return SlackChannel.builder()
            .channelName("Panda team - slack")
            .slackUrl("")
            .build();
    }
}
