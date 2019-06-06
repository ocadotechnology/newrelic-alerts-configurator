package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.SlackChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class SlackChannelDsl {
    var channelName: String? = null
    var slackUrl: String? = null
    var teamChannel: String? = null
}

fun slackChannel(block: SlackChannelDsl.() -> Unit): SlackChannel {
    val dsl = SlackChannelDsl()
    dsl.block()

    return SlackChannel.builder()
            .channelName(dsl.channelName)
            .slackUrl(dsl.slackUrl)
            .teamChannel(dsl.teamChannel)
            .build()
}