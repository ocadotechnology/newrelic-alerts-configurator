package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmJvmCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.violationclosetimer.ViolationCloseTimer
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class ApmJvmConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var applications: Collection<String> = mutableListOf()
    var metric: ApmJvmCondition.Metric? = null
    var gcMetric: ApmJvmCondition.GcMetric? = null
    var runBookUrl: String? = null
    var violationCloseTimer: ViolationCloseTimer? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)
}

fun apmJvmCondition(block: ApmJvmConditionDsl.() -> Unit): ApmJvmCondition {
    val dsl = ApmJvmConditionDsl()
    dsl.block()

    return ApmJvmCondition.builder()
            .conditionName(dsl.conditionName)
            .enabled(dsl.enabled)
            .applications(dsl.applications)
            .metric(dsl.metric)
            .gcMetric(dsl.gcMetric)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .violationCloseTimer(dsl.violationCloseTimer)
            .build()
}