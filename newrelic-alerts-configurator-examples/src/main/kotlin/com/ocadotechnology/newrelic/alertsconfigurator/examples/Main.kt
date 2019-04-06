package com.ocadotechnology.newrelic.alertsconfigurator.examples

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.policy
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configurator

object APP1 {
    private const val NAME = "Application1"
    val applicationConfig = applicationConfiguration(NAME)
    val policyConfig = policy {
        policyName = "Application1 Policy"
        incidentPreference = PolicyConfiguration.IncidentPreference.PER_POLICY

        conditions {
            +apdexCondition(NAME)
            +diskSpaceCondition("app-1-host")
            +heapUsageCondition(NAME)
        }

        channels {
            +teamEmail()
            +slack()
        }
    }
}

object APP2 {
    private const val NAME = "Application2"
    val applicationConfig = applicationConfiguration(NAME)
    val policyConfig = policy {
        policyName = "Application2 Policy"
        incidentPreference = PolicyConfiguration.IncidentPreference.PER_POLICY

        conditions {
            +apdexCondition(NAME)
            +heapUsageCondition(NAME)
            +jsErrorsCondition(NAME)
            +healthCheckCondition(NAME)
        }

        channels {
            +slack()
        }
    }
}

object APP3 {
    private const val NAME = "Application3"
    val applicationConfig = applicationConfiguration(NAME)
    val policyConfig = policy {
        policyName = "Application3 Policy"
        incidentPreference = PolicyConfiguration.IncidentPreference.PER_POLICY

        conditions {
            +apdexCondition(NAME)
            +cpuUsageCondition("app-3-host")
            +heapUsageCondition(NAME)
        }

        channels {
            +teamEmail()
            +slack()
        }
    }
}

fun main() {
    val stagingEnvironmentNewRelicKey = "KEY1"
    val productionEnvironmentNewRelicKey = "KEY2"

    configure(stagingEnvironmentNewRelicKey)
    configure(productionEnvironmentNewRelicKey)
}

fun configure(newRelicKey: String) {
    configurator(newRelicKey) {
        applications {
            +APP1.applicationConfig
            +APP2.applicationConfig
            +APP3.applicationConfig
        }
        policies {
            +APP1.policyConfig
            +APP2.policyConfig
            +APP3.policyConfig
        }
    }
}