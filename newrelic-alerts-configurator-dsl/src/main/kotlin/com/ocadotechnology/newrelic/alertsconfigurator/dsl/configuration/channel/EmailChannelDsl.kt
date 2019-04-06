package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.EmailChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class EmailChannelDsl {
    var channelName: String? = null
    var emailAddress: String? = null
    var includeJsonAttachment: Boolean = false
}

fun emailChannel(block: EmailChannelDsl.() -> Unit): EmailChannel {
    val dsl = EmailChannelDsl()
    dsl.block()

    return EmailChannel.builder()
            .channelName(dsl.channelName)
            .emailAddress(dsl.emailAddress)
            .includeJsonAttachment(dsl.includeJsonAttachment)
            .build()
}