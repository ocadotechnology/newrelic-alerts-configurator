package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class UserDefinedConfigurationDsl {
    var metric: String? = null
    var valueFunction: UserDefinedConfiguration.ValueFunction? = null
}

fun userDefinedConfiguration(block: UserDefinedConfigurationDsl.() -> Unit): UserDefinedConfiguration {
    val dsl = UserDefinedConfigurationDsl()
    dsl.block()

    return UserDefinedConfiguration.builder()
            .metric(requireNotNull(dsl.metric) { "User defined configuration metric cannot be null" })
            .valueFunction(requireNotNull(dsl.valueFunction) { "User defined configuration value cannot be null" })
            .build()
}