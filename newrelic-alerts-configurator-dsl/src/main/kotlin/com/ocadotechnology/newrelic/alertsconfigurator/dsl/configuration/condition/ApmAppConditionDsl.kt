package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmAppCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class ApmAppConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var applications: Collection<String> = mutableListOf()
    var metric: ApmAppCondition.Metric? = null
    var conditionScope: ApmAppCondition.ConditionScope? = null
    var runBookUrl: String? = null
    var violationCloseTimer: ViolationCloseTimer? = null
    var userDefinedConfiguration: UserDefinedConfiguration? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)

    fun userDefined(block: UserDefinedConfigurationDsl.() -> Unit) {
        userDefinedConfiguration = userDefinedCondition(block)
    }
}

fun apmAppCondition(block: ApmAppConditionDsl.() -> Unit): ApmAppCondition {
    val dsl = ApmAppConditionDsl()
    dsl.block()

    return ApmAppCondition.builder()
            .conditionName(dsl.conditionName)
            .enabled(dsl.enabled)
            .applications(dsl.applications)
            .metric(dsl.metric)
            .conditionScope(dsl.conditionScope)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .violationCloseTimer(dsl.violationCloseTimer)
            .userDefinedConfiguration(dsl.userDefinedConfiguration)
            .build()
}

