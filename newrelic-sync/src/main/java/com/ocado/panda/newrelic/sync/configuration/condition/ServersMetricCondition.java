package com.ocado.panda.newrelic.sync.configuration.condition;

import com.ocado.panda.newrelic.sync.configuration.condition.terms.TermsConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;

/**
 * Servers metric configuration.
 * Configuration parameters:
 * <ul>
 *     <li>{@link #conditionName}</li>
 *     <li>{@link #enabled}</li>
 *     <li>{@link #entities}</li>
 *     <li>{@link #metric}</li>
 *     <li>{@link #runBookUrl}</li>
 *     <li>{@link #terms}</li>
 * </ul>
 */
@Getter
@Builder
public class ServersMetricCondition implements Condition {
    private final ConditionType type = ConditionType.SERVERS_METRIC;
    /**
     * Name of your servers metric condition.
     */
    @NonNull
    private String conditionName;
    /**
     * If your servers metric condition is enabled. Default is false.
     */
    private boolean enabled;
    /**
     * Collection of server names for which this condition is applied.
     * If server with given name does not exist exception will be thrown.
     */
    @NonNull
    @Singular
    private Collection<String> entities;
    /**
     * Metric used in given condition.
     */
    @NonNull
    private Metric metric;
    /**
     * The runbook URL to display in notifications.
     */
    private String runBookUrl;
    /**
     * Collection of terms used for alerts condition.
     */
    @NonNull
    @Singular
    private Collection<TermsConfiguration> terms;

    @Override
    public String getMetricAsString() {
        return metric.name().toLowerCase();
    }

    public enum Metric {
        CPU_PERCENTAGE, DISK_IO_PERCENTAGE, MEMORY_PERCENTAGE, FULLEST_DISK_PERCENTAGE, LOAD_AVERAGE_ONE_MINUTE
    }
}
