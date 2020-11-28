package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.ExpirationConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.lossofsignal.SignalConfiguration
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
    var sinceValue: NrqlCondition.SinceValue? = null
    internal val terms: MutableList<NrqlTermsConfiguration> = mutableListOf()
    var expiration: ExpirationConfiguration? = null
    var signal: SignalConfiguration? = null

    fun terms(block: NrqlTermConfigurations.() -> Unit) = terms.addAll(NrqlTermConfigurations().apply(block).terms)
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
            .sinceValue(requireNotNull(dsl.sinceValue) { "Nrql condition since cannot be null" })
            .expiration(dsl.expiration)
            .signal(dsl.signal)
            .build()
}