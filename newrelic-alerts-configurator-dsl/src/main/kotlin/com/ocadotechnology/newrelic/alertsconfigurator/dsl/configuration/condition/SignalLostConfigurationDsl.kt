package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.signal.SignalLostConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class SignalLostConfigurationDsl {
    var signalIsLostAfter: Int? = null
    var openNewViolationOnSignalLost: Boolean = false
    var closeCurrentViolationsOnSignalLost: Boolean = false
}

fun signalLostConfiguration(block: SignalLostConfigurationDsl.() -> Unit): SignalLostConfiguration {
    val dsl = SignalLostConfigurationDsl()
    dsl.block()

    return SignalLostConfiguration.builder()
        .signalIsLostAfter(requireNotNull(dsl.signalIsLostAfter, { "Signal is lost after cannot be null" }))
        .openNewViolationOnSignalLost(dsl.openNewViolationOnSignalLost)
        .closeCurrentViolationsOnSignalLost(dsl.closeCurrentViolationsOnSignalLost)
        .build()
}