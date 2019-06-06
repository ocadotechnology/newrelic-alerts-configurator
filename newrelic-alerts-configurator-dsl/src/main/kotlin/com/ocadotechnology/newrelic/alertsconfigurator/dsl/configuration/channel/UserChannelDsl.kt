package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.UserChannel
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class UserChannelDsl {
    var userEmail: String? = null
}

fun userChannel(block: UserChannelDsl.() -> Unit): UserChannel {
    val dsl = UserChannelDsl()
    dsl.block()

    return UserChannel.builder()
            .userEmail(dsl.userEmail)
            .build()
}