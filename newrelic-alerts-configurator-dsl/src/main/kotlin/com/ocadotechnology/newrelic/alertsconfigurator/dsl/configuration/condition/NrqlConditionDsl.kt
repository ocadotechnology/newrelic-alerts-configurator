package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.NrqlTermConfigurations

@NewRelicConfigurationMarker
class NrqlConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var runBookUrl: String? = null
    var valueFunction: NrqlCondition.ValueFunction? = null
    var query: String? = null
    internal val terms: MutableList<NrqlTermsConfiguration> = mutableListOf()
    var nrqlSignalConfiguration: NrqlSignalConfiguration? = null

    fun terms(block: NrqlTermConfigurations.() -> Unit) = terms.addAll(NrqlTermConfigurations().apply(block).terms)

    fun signal(block: NrqlSignalConfigurationDsl.() -> Unit) {
        nrqlSignalConfiguration = nrqlSignalConfiguration(block)
    }
}

fun nrqlCondition(block: NrqlConditionDsl.() -> Unit): NrqlCondition {
    val dsl = NrqlConditionDsl()
    dsl.block()

    return NrqlCondition.builder()
            .conditionName(requireNotNull(dsl.conditionName) { "Nrql condition name cannot be null" })
            .enabled(dsl.enabled)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .valueFunction(requireNotNull(dsl.valueFunction) { "Nrql condition value cannot be null" })
            .query(requireNotNull(dsl.query) { "Nrql condition query cannot be null" })
            .signal(dsl.nrqlSignalConfiguration)
            .build()
}