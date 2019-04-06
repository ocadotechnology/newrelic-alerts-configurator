package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class ApplicationConfigurationDsl {
    var applicationName: String = ""
    var appApdexThreshold: Float = 0.0f
    var endUserApdexThreshold: Float = 0.0f
    var enableRealUserMonitoring: Boolean = false
}

fun application(block: ApplicationConfigurationDsl.() -> Unit): ApplicationConfiguration {
    val dsl = ApplicationConfigurationDsl()
    dsl.block()

    return ApplicationConfiguration.builder()
            .applicationName(dsl.applicationName)
            .appApdexThreshold(dsl.appApdexThreshold)
            .endUserApdexThreshold(dsl.endUserApdexThreshold)
            .enableRealUserMonitoring(dsl.enableRealUserMonitoring)
            .build()
}

@NewRelicConfigurationMarker
class ApplicationConfigurations {
    internal val applications:  MutableList<ApplicationConfiguration> = mutableListOf()

    fun application(block: ApplicationConfigurationDsl.() -> Unit) = applications.add(com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.application(block))
    operator fun ApplicationConfiguration.unaryPlus() = applications.add(this)
}
