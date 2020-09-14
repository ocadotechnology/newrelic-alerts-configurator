package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.*
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class TermsConfigurationDsl {
    var durationTerm: DurationTerm? = null
    var operatorTerm: OperatorTerm? = null
    var priorityTerm: PriorityTerm? = null
    var thresholdTerm: Float? = null
    var timeFunctionTerm: TimeFunctionTerm? = null
}

fun termsConfiguration(block: TermsConfigurationDsl.() -> Unit): TermsConfiguration {
    val dsl = TermsConfigurationDsl()
    dsl.block()

    return TermsConfiguration.builder()
            .durationTerm(requireNotNull(dsl.durationTerm) { "Terms duration cannot be null" })
            .operatorTerm(requireNotNull(dsl.operatorTerm) { "Terms operator cannot be null" })
            .priorityTerm(requireNotNull(dsl.priorityTerm) { "Terms priority cannot be null" })
            .thresholdTerm(requireNotNull(dsl.thresholdTerm) { "Terms threshold cannot be null" })
            .timeFunctionTerm(requireNotNull(dsl.timeFunctionTerm) { "Terms time cannot be null" })
            .build()
}

@NewRelicConfigurationMarker
class TermConfigurations {
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun term(block: TermsConfigurationDsl.() -> Unit) = terms.add(com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.termsConfiguration(block))
    operator fun TermsConfiguration.unaryPlus() = terms.add(this)

    infix fun PriorityTerm.given(timeFunctionTerm: TimeFunctionTerm) : AfterGiven {
        return AfterGiven(TermsConfiguration.builder().priorityTerm(this).timeFunctionTerm(timeFunctionTerm))
    }

    infix fun AfterGiven.inLast(durationTerm: DurationTerm) : AfterInLast {
        return AfterInLast(this.builder.durationTerm(durationTerm))
    }

    infix fun AfterInLast.minutes(operatorTerm: OperatorTerm) : AfterMinutes {
        return AfterMinutes(this.builder.operatorTerm(operatorTerm))
    }

    infix fun AfterMinutes.value(thresholdTerm: Float) {
        terms.add(this.builder.thresholdTerm(thresholdTerm).build())
    }

    class AfterGiven(internal val builder: TermsConfiguration.TermsConfigurationBuilder)
    class AfterInLast(internal val builder: TermsConfiguration.TermsConfigurationBuilder)
    class AfterMinutes(internal val builder: TermsConfiguration.TermsConfigurationBuilder)
}