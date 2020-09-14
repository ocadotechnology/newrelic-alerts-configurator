package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ApmExternalServiceCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms.TermsConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.terms.TermConfigurations

@NewRelicConfigurationMarker
class ApmExternalServiceConditionDsl {
    var conditionName: String? = null
    var enabled: Boolean = true
    var applications: Collection<String> = mutableListOf()
    var externalServiceUrl: String? = null
    var metric: ApmExternalServiceCondition.Metric? = null
    var runBookUrl: String? = null
    internal val terms: MutableList<TermsConfiguration> = mutableListOf()

    fun terms(block: TermConfigurations.() -> Unit) = terms.addAll(TermConfigurations().apply(block).terms)
}

fun apmExternalServiceCondition(block: ApmExternalServiceConditionDsl.() -> Unit): ApmExternalServiceCondition {
    val dsl = ApmExternalServiceConditionDsl()
    dsl.block()

    return ApmExternalServiceCondition.builder()
            .conditionName(requireNotNull(dsl.conditionName) { "Apm external service condition name cannot be null" })
            .enabled(dsl.enabled)
            .applications(dsl.applications)
            .externalServiceUrl(requireNotNull(dsl.externalServiceUrl) { "Apm external service condition url cannot be null" })
            .metric(requireNotNull(dsl.metric) { "Apm external service condition metric cannot be null" })
            .runBookUrl(dsl.runBookUrl)
            .terms(dsl.terms)
            .build()
}