package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.PagerDutyChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class PagerDutyChannelDsl {
    var channelName: String? = null
    var serviceKey: String? = null
}

fun pagerDutyChannel(block: PagerDutyChannelDsl.() -> Unit): PagerDutyChannel {
    val dsl = PagerDutyChannelDsl()
    dsl.block()

    return PagerDutyChannel.builder()
            .channelName(requireNotNull(dsl.channelName) { "PagerDuty channel name cannot be null" })
            .serviceKey(requireNotNull(dsl.serviceKey) { "PagerDuty channel key cannot be null" })
            .build()
}