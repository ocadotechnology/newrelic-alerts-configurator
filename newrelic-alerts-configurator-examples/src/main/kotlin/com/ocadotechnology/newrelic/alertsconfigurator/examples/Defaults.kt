package com.ocadotechnology.newrelic.alertsconfigurator.examples

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.*
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.*
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.applicationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel.emailChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel.slackChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.*

fun applicationConfiguration(name: String): ApplicationConfiguration {
    return applicationConfiguration {
        applicationName = name
        appApdexThreshold = 0.5f
        endUserApdexThreshold = 0.7f
        enableRealUserMonitoring = true
    }
}

fun apdexCondition(applicationName: String): Condition {
    return apmAppCondition {
        conditionName = "Apdex"
        enabled = true
        applications = listOf(applicationName)
        metric = ApmAppCondition.Metric.APDEX
        conditionScope = ApmAppCondition.ConditionScope.APPLICATION

        terms {
            PriorityTerm.CRITICAL given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.BELOW value 0.7f
            PriorityTerm.WARNING given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.BELOW value 0.85f
        }
    }
}

fun diskSpaceCondition(vararg serverNames: String): Condition {
    return serversMetricCondition {
        conditionName = "Fullest Disk %"
        enabled = true
        servers = listOf(*serverNames)
        metric = ServersMetricCondition.Metric.FULLEST_DISK_PERCENTAGE

        terms {
            PriorityTerm.CRITICAL given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 80.0f
            PriorityTerm.WARNING given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 65.0f
        }
    }
}

fun cpuUsageCondition(vararg serverNames: String): Condition {
    return serversMetricCondition {
        conditionName = "CPU Usage %"
        enabled = true
        servers = listOf(*serverNames)
        metric = ServersMetricCondition.Metric.CPU_PERCENTAGE

        terms {
            PriorityTerm.CRITICAL given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 90.0f
            PriorityTerm.WARNING given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 70.0f
        }
    }
}

fun heapUsageCondition(applicationName: String): Condition {
    return apmJvmCondition {
        conditionName = "Heap usage"
        enabled = true
        applications = listOf(applicationName)
        metric = ApmJvmCondition.Metric.HEAP_MEMORY_USAGE
        violationCloseTimer = ViolationCloseTimer.DURATION_24

        terms {
            PriorityTerm.CRITICAL given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 85.0f
            PriorityTerm.WARNING given TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 65.0f
        }
    }
}

fun healthCheckCondition(applicationName: String): NrqlCondition {
    return nrqlCondition {
        conditionName = "Health Check"
        enabled = true
        valueFunction = NrqlCondition.ValueFunction.SINGLE_VALUE
        sinceValue = NrqlCondition.SinceValue.SINCE_5
        query = "SELECT count(*) FROM `$applicationName:HealthStatus` WHERE healthy IS false"

        terms {
            term {
                priorityTerm = PriorityTerm.CRITICAL
                durationTerm = NrqlDurationTerm.DURATION_1
                timeFunctionTerm = TimeFunctionTerm.ANY
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 0.0f
            }
        }
    }
}

fun jsErrorsCondition(applicationName: String): BrowserCondition {
    return browserCondition {
        conditionName = "Page views with JS errors"
        enabled = true
        applications = listOf(applicationName)
        metric = BrowserCondition.Metric.PAGE_VIEWS_WITH_JS_ERRORS

        terms {
            term {
                priorityTerm = PriorityTerm.WARNING
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ANY
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 1.0f
            }
        }
    }
}

fun teamEmail(): Channel {
    return emailChannel {
        channelName = "My Team - email"
        emailAddress = "my-team@my-company.com"
        includeJsonAttachment = true
    }
}

fun slack(): Channel {
    return slackChannel {
        channelName = "My Team - slack"
        slackUrl = "https://hooks.slack.com/services/aaaaaaaaa/bbbbbbbbb/cccccccccccccccccccccccc"
        teamChannel = "newrelic-alerts"
    }
}