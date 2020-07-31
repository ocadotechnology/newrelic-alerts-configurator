package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.BrowserCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class BrowserConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var applications: Collection<String> = mutableListOf()
    var metric: BrowserCondition.Metric? = null
    var runBookUrl: String? = null
    var userDefinedConfiguration: UserDefinedConfiguration? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)

    fun userDefined(block: UserDefinedConfigurationDsl.() -> Unit) {
        userDefinedConfiguration = userDefinedConfiguration(block)
    }
}

fun browserCondition(block: BrowserConditionDsl.() -> Unit): BrowserCondition {
    val dsl = BrowserConditionDsl()
    dsl.block()

    return BrowserCondition.builder()
            .conditionName(requireNotNull(dsl.conditionName) { "Browser condition name cannot be null" })
            .enabled(dsl.enabled)
            .applications(dsl.applications)
            .metric(requireNotNull(dsl.metric) { "Browser condition metric cannot be null" })
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .userDefinedConfiguration(dsl.userDefinedConfiguration)
            .build()
}