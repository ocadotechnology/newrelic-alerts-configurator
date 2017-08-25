package com.ocadotechnology.newrelic.alertsconfigurator.sample;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.EmailChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.SlackChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ServersMetricCondition;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.DurationTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm;

import java.util.Arrays;

/**
 * Utility class used to obtain {@link Channel}, {@link Condition} or {@link ApplicationConfiguration} predefined
 * with default values.
 */
public final class Defaults {


    private Defaults() {
    }

    public static ApplicationConfiguration applicationConfiguration(String applicationName) {
        return ApplicationConfiguration.builder()
                .applicationName(applicationName)
                .appApdexThreshold(0.5f)
                .endUserApdexThreshold(0.7f)
                .enableRealUserMonitoring(true)
                .build();
    }

    public static Condition apdexCondition(String applicationName) {
        return ApmAppCondition.builder()
                .conditionName("Apdex")
                .enabled(true)
                .application(applicationName)
                .metric(ApmAppCondition.Metric.APDEX)
                .conditionScope(ApmAppCondition.ConditionScope.APPLICATION)
                // Raise critical if in the last 5 minutes apdex was below 0.7
                .term(TermsConfiguration.builder()
                        .durationTerm(DurationTerm.DURATION_5)
                        .operatorTerm(OperatorTerm.BELOW)
                        .priorityTerm(PriorityTerm.CRITICAL)
                        .timeFunctionTerm(TimeFunctionTerm.ALL)
                        .thresholdTerm(0.7f)
                        .build()
                )
                // Raise warning if in the last 5 minutes apdex was below 0.85
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

    public static Condition diskSpaceCondition(String... serverNames) {
        return ServersMetricCondition.builder()
                .conditionName("Fullest Disk %")
                .enabled(true)
                .servers(Arrays.asList(serverNames))
                .metric(ServersMetricCondition.Metric.FULLEST_DISK_PERCENTAGE)
                // Raise critical if in the last 5 minutes disk was over 80% full
                .term(TermsConfiguration.builder()
                        .priorityTerm(PriorityTerm.CRITICAL)
                        .durationTerm(DurationTerm.DURATION_5)
                        .operatorTerm(OperatorTerm.ABOVE)
                        .timeFunctionTerm(TimeFunctionTerm.ALL)
                        .thresholdTerm(80.0f)
                        .build()
                )
                // Raise warning if in the last 5 minutes disk was over 65% full
                .term(TermsConfiguration.builder()
                        .priorityTerm(PriorityTerm.WARNING)
                        .durationTerm(DurationTerm.DURATION_5)
                        .operatorTerm(OperatorTerm.ABOVE)
                        .timeFunctionTerm(TimeFunctionTerm.ALL)
                        .thresholdTerm(65.0f)
                        .build()
                )
                .build();
    }

    public static Condition cpuUsageCondition(String... serverNames) {
        return ServersMetricCondition.builder()
                .conditionName("CPU Usage %")
                .enabled(true)
                .servers(Arrays.asList(serverNames))
                .metric(ServersMetricCondition.Metric.CPU_PERCENTAGE)
                // Raise critical if in the last 5 minutes cpu was over 90%
                .term(TermsConfiguration.builder()
                        .priorityTerm(PriorityTerm.CRITICAL)
                        .durationTerm(DurationTerm.DURATION_5)
                        .operatorTerm(OperatorTerm.ABOVE)
                        .timeFunctionTerm(TimeFunctionTerm.ALL)
                        .thresholdTerm(90.0f)
                        .build()
                )
                // Raise warning if in the last 5 minutes cpu was over 70%
                .term(TermsConfiguration.builder()
                        .priorityTerm(PriorityTerm.WARNING)
                        .durationTerm(DurationTerm.DURATION_5)
                        .operatorTerm(OperatorTerm.ABOVE)
                        .timeFunctionTerm(TimeFunctionTerm.ALL)
                        .thresholdTerm(70.0f)
                        .build()
                )
                .build();
    }

    public static Channel teamEmailChannel() {
        return EmailChannel.builder()
                .channelName("My Team - email")
                .emailAddress("my-team@my-company.com")
                .includeJsonAttachment(true)
                .build();
    }

    public static Channel slackChannel() {
        return SlackChannel.builder()
                .channelName("My Team - slack")
                .slackUrl("https://hooks.slack.com/services/aaaaaaaaa/bbbbbbbbb/cccccccccccccccccccccccc")
                .teamChannel("newrelic-alerts")
                .build();
    }
}
