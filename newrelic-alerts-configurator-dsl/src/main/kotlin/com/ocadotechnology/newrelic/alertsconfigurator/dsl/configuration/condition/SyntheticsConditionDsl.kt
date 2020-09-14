package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.SyntheticsCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class SyntheticsConditionDsl {
    var conditionName: String? = null
    var monitorName: String? = null
    var enabled: Boolean = true
    var runBookUrl: String? = null
}

fun syntheticsCondition(block: SyntheticsConditionDsl.() -> Unit): SyntheticsCondition {
    val dsl = SyntheticsConditionDsl()
    dsl.block()

    return SyntheticsCondition.builder()
            .conditionName(requireNotNull(dsl.conditionName) { "Synthetics condition name cannot be null" })
            .monitorName(requireNotNull(dsl.monitorName) { "Synthetics condition monitor name cannot be null" })
            .enabled(dsl.enabled)
            .runBookUrl(dsl.runBookUrl)
            .build()
}