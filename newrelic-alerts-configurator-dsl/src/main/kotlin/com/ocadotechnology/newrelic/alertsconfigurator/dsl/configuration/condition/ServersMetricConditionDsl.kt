package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ServersMetricCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class ServersMetricConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var servers: Collection<String> = mutableListOf()
    var metric: ServersMetricCondition.Metric? = null
    var runBookUrl: String? = null
    var userDefinedConfiguration: UserDefinedConfiguration? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)

    fun userDefined(block: UserDefinedConfigurationDsl.() -> Unit) {
        userDefinedConfiguration = userDefinedCondition(block)
    }
}

fun serversMetricCondition(block: ServersMetricConditionDsl.() -> Unit): ServersMetricCondition {
    val dsl = ServersMetricConditionDsl()
    dsl.block()

    return ServersMetricCondition.builder()
            .conditionName(dsl.conditionName)
            .enabled(dsl.enabled)
            .servers(dsl.servers)
            .metric(dsl.metric)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .userDefinedConfiguration(dsl.userDefinedConfiguration)
            .build()
}