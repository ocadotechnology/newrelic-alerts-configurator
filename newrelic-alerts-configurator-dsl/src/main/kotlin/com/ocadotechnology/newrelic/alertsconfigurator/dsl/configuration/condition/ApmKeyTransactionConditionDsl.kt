package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmKeyTransactionCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class ApmKeyTransactionConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var keyTransactions: Collection<String> = mutableListOf()
    var metric: ApmKeyTransactionCondition.Metric? = null
    var runBookUrl: String? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)
}

fun apmKeyTransactionCondition(block: ApmKeyTransactionConditionDsl.() -> Unit): ApmKeyTransactionCondition {
    val dsl = ApmKeyTransactionConditionDsl()
    dsl.block()

    return ApmKeyTransactionCondition.builder()
            .conditionName(dsl.conditionName)
            .enabled(dsl.enabled)
            .keyTransactions(dsl.keyTransactions)
            .metric(dsl.metric)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .build()
}