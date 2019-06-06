package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class Channels {
    internal val channels: MutableList<Channel> = mutableListOf()

    fun email(block: EmailChannelDsl.() -> Unit) = channels.add(emailChannel(block))
    fun pagerDuty(block: PagerDutyChannelDsl.() -> Unit) = channels.add(pagerDutyChannel(block))
    fun slack(block: SlackChannelDsl.() -> Unit) = channels.add(slackChannel(block))
    fun user(block: UserChannelDsl.() -> Unit) = channels.add(userChannel(block))
    fun webhook(block: WebhookChannelDsl.() -> Unit) = channels.add(webhookChannel(block))
    operator fun Channel.unaryPlus() = channels.add(this)
}