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
            .channelName(requireNotNull(dsl.channelName) { "Slack channel name cannot be null" })
            .slackUrl(requireNotNull(dsl.slackUrl) {"Slack channel url cannot be null"})
            .teamChannel(dsl.teamChannel)
            .build()
}