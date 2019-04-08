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

fun termConfiguration(block: TermsConfigurationDsl.() -> Unit): TermsConfiguration {
    val dsl = TermsConfigurationDsl()
    dsl.block()

    return TermsConfiguration.builder()
            .durationTerm(dsl.durationTerm)
            .operatorTerm(dsl.operatorTerm)
            .priorityTerm(dsl.priorityTerm)
            .thresholdTerm(dsl.thresholdTerm)
            .timeFunctionTerm(dsl.timeFunctionTerm)
            .build()
}

@NewRelicConfigurationMarker
class TermConfigurations {
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun term(block: TermsConfigurationDsl.() -> Unit) = terms.add(com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.termConfiguration(block))
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