package com.ocadotechnology.newrelic.alertsconfigurator.examples

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmJvmCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.BrowserCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ServersMetricCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.DurationTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlDurationTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.application
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel.emailChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel.slackChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.apmAppCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.apmJvmCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.browserCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.nrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.serversMetricCondition

fun applicationConfiguration(name: String): ApplicationConfiguration {
    return application {
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
            // Raise critical if in the last 5 minutes apdex was below 0.7
            term {
                priorityTerm = PriorityTerm.CRITICAL
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.BELOW
                thresholdTerm = 0.7f
            }

            // Raise warning if in the last 5 minutes apdex was below 0.85
            term {
                priorityTerm = PriorityTerm.WARNING
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.BELOW
                thresholdTerm = 0.85f
            }
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
            // Raise critical if in the last 5 minutes disk was over 80% full
            term {
                priorityTerm = PriorityTerm.CRITICAL
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 80.0f
            }

            // Raise warning if in the last 5 minutes disk was over 65% full
            term {
                priorityTerm = PriorityTerm.WARNING
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 65.0f
            }
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
            // Raise critical if in the last 5 minutes cpu was over 90%
            term {
                priorityTerm = PriorityTerm.CRITICAL
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 90.0f
            }

            // Raise warning if in the last 5 minutes cpu was over 70%
            term {
                priorityTerm = PriorityTerm.WARNING
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 70.0f
            }
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
            // Raise critical if in the last 5 minutes heap memory usage was above 85%
            term {
                // TODO maybe some flux stuff?
                // PriorityTerm.CRITICAL when TimeFunctionTerm.ALL inLast DurationTerm.DURATION_5 minutes OperatorTerm.ABOVE value 85.0f
                priorityTerm = PriorityTerm.CRITICAL
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 85.0f
            }

            // Raise warning if in the last 5 minutes cpu was over 70%
            term {
                priorityTerm = PriorityTerm.WARNING
                durationTerm = DurationTerm.DURATION_5
                timeFunctionTerm = TimeFunctionTerm.ALL
                operatorTerm = OperatorTerm.ABOVE
                thresholdTerm = 65.0f
            }
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