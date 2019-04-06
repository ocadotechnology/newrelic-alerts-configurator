package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class UserDefinedConfigurationDsl {
    var metric: String? = null
    var valueFunction: UserDefinedConfiguration.ValueFunction? = null
}

fun userDefined(block: UserDefinedConfigurationDsl.() -> Unit): UserDefinedConfiguration {
    val dsl = UserDefinedConfigurationDsl()
    dsl.block()

    return UserDefinedConfiguration.builder()
            .metric(dsl.metric)
            .valueFunction(dsl.valueFunction)
            .build()
}