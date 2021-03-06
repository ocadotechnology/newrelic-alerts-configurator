package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalFillOption
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalLostConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class NrqlSignalConfigurationDsl {
    var aggregationWindow: Int? = null
    var evaluationWindows: Int? = null
    var signalFillOption: SignalFillOption? = null
    var signalLostConfiguration: SignalLostConfiguration? = null

    fun signalLost(block: SignalLostConfigurationDsl.() -> Unit) {
        signalLostConfiguration = signalLostConfiguration(block)
    }
}

fun nrqlSignalConfiguration(block: NrqlSignalConfigurationDsl.() -> Unit): NrqlSignalConfiguration {
    val dsl = NrqlSignalConfigurationDsl()
    dsl.block()

    return NrqlSignalConfiguration.builder()
        .aggregationWindow(requireNotNull(dsl.aggregationWindow) { "Aggregation window cannot be null" })
        .evaluationWindows(requireNotNull(dsl.evaluationWindows) { "Evaluation window cannot be null" })
        .signalFillOption(requireNotNull(dsl.signalFillOption) { "Signal fill option cannot be null" })
        .signalLostConfiguration(requireNotNull(dsl.signalLostConfiguration) { "Signal lost configuration cannot be null" })
        .build()
}