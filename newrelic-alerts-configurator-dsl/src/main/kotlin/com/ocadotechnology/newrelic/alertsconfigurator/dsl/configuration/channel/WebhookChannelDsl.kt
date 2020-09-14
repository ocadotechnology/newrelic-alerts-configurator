package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.WebhookChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class WebhookChannelDsl {
    var channelName: String? = null
    var baseUrl: String? = null
    val authUsername: String? = null
    val authPassword: String? = null
}

fun webhookChannel(block: WebhookChannelDsl.() -> Unit): WebhookChannel {
    val dsl = WebhookChannelDsl()
    dsl.block()

    return WebhookChannel.builder()
            .channelName(requireNotNull(dsl.channelName) { "Webhook channel name cannot be null" })
            .baseUrl(requireNotNull(dsl.baseUrl) { "Webhook channel url cannot be null" })
            .authUsername(dsl.authUsername)
            .authPassword(dsl.authPassword)
            .build()
}