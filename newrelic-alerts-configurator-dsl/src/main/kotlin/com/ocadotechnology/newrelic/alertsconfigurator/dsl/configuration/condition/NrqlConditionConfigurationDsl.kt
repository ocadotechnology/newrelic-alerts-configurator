package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.NrqlSignalConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalFillOption
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalLostConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class NrqlSignalConfigurationDsl {
    var aggregationWindow: Int? = null
    var evaluationWindows: Int? = null
    var signalFillOption: SignalFillOption? = null
    var signalFillValue: String? = null
    var signalLostConfiguration: SignalLostConfiguration? = null

    fun signalLost(block: SignalLostConfigurationDsl.() -> Unit) {
        signalLostConfiguration = signalLostConfiguration(block)
    }
}

fun nrqlSignalConfiguration(block: NrqlSignalConfigurationDsl.() -> Unit): NrqlSignalConfiguration {
    val dsl = NrqlSignalConfigurationDsl()
    dsl.block()

    val signalConfigurationBuilder = NrqlSignalConfiguration.builder()
            .aggregationWindow(requireNotNull(dsl.aggregationWindow) { "Aggregation window cannot be null" })
            .evaluationWindows(requireNotNull(dsl.evaluationWindows) { "Evaluation window cannot be null" })
            .signalFillOption(requireNotNull(dsl.signalFillOption) { "Signal fill option cannot be null" })
            .signalLostConfiguration(dsl.signalLostConfiguration)
    if (dsl.signalFillOption == SignalFillOption.STATIC) {
        signalConfigurationBuilder.signalFillValue(requireNotNull(dsl.signalFillValue) {"Signal fill value is required when signal fill is STATIC"})
    }
    return signalConfigurationBuilder
        .build()
}
