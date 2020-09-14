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
            .conditionName(requireNotNull(dsl.conditionName) { "Apm jvm condition name cannot be null" })
            .enabled(dsl.enabled)
            .applications(dsl.applications)
            .metric(requireNotNull(dsl.metric) { "Apm jvm condition metric cannot be null" })
            .gcMetric(dsl.gcMetric)
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .violationCloseTimer(requireNotNull(dsl.violationCloseTimer) { "Apm jvm condition timer cannot be null" })
            .build()
}