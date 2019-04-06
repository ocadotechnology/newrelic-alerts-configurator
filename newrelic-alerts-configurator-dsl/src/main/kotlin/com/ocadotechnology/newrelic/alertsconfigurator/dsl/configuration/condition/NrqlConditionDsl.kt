package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
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

    fun terms(block: NrqlTermConfigurations.() -> Unit) = terms.addAll(NrqlTermConfigurations().apply(block).terms)
}

fun nrqlCondition(block: NrqlConditionDsl.() -> Unit): NrqlCondition {
    val dsl = NrqlConditionDsl()
    dsl.block()

    return NrqlCondition.builder()
            .conditionName(dsl.conditionName)
            .enabled(dsl.enabled)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .valueFunction(dsl.valueFunction)
            .query(dsl.query)
            .sinceValue(dsl.sinceValue)
            .build()
}