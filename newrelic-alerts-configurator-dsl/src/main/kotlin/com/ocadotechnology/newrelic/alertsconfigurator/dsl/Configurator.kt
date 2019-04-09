package com.ocadotechnology.newrelic.alertsconfigurator.dsl

import com.ocadotechnology.newrelic.alertsconfigurator.Configurator
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.ApplicationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.ApplicationConfigurations
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.PolicyConfigurations

// TODO Add README.md - index
// TODO Add README.md - + operator
// TODO Add README.md - term and nrqlTerm flux
@NewRelicConfigurationMarker
class ConfiguratorDsl {
    internal val applicationConfigurations: MutableList<ApplicationConfiguration> = mutableListOf()
    internal val policyConfigurations: MutableList<PolicyConfiguration> = mutableListOf()

    fun applications(block: ApplicationConfigurations.() -> Unit) = applicationConfigurations.addAll(ApplicationConfigurations().apply(block).applications)
    fun policies(block: PolicyConfigurations.() -> Unit) = policyConfigurations.addAll(PolicyConfigurations().apply(block).policies)
}

fun configurator(apiKey: String, block: ConfiguratorDsl.() -> Unit) {
    val dsl = ConfiguratorDsl()
    dsl.apply(block)

    val configurator = Configurator(apiKey)
    configurator.setApplicationConfigurations(dsl.applicationConfigurations)
    configurator.setPolicyConfigurations(dsl.policyConfigurations)
    configurator.sync()
}