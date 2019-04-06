package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlDurationTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.NrqlTermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.OperatorTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.PriorityTerm
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TimeFunctionTerm
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class NrqlTermsConfigurationDsl {
    var durationTerm: NrqlDurationTerm? = null
    var operatorTerm: OperatorTerm? = null
    var priorityTerm: PriorityTerm? = null
    var thresholdTerm: Float? = null
    var timeFunctionTerm: TimeFunctionTerm? = null
}

fun nrqlTerm(block: NrqlTermsConfigurationDsl.() -> Unit): NrqlTermsConfiguration {
    val dsl = NrqlTermsConfigurationDsl()
    dsl.block()

    return NrqlTermsConfiguration.builder()
            .durationTerm(dsl.durationTerm)
            .operatorTerm(dsl.operatorTerm)
            .priorityTerm(dsl.priorityTerm)
            .thresholdTerm(dsl.thresholdTerm)
            .timeFunctionTerm(dsl.timeFunctionTerm)
            .build()
}

@NewRelicConfigurationMarker
class NrqlTermConfigurations {
    internal val terms: MutableList<NrqlTermsConfiguration> = mutableListOf()

    fun term(block: NrqlTermsConfigurationDsl.() -> Unit) = terms.add(com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.nrqlTerm(block))
    operator fun NrqlTermsConfiguration.unaryPlus() = terms.add(this)
}