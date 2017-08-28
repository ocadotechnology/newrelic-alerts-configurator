package com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.internal;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.ChannelTypeSupport;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.UserChannel;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannelConfiguration;
import com.ocadotechnology.newrelic.apiclient.model.users.User;
import lombok.AllArgsConstructor;

import static java.lang.String.format;

@AllArgsConstructor
public class UserChannelTypeSupport implements ChannelTypeSupport {
    private Channel channel;

    @Override
    public AlertsChannelConfiguration generateAlertsChannelConfiguration(NewRelicApi api) {
        UserChannel userChannel = (UserChannel) channel;
        int userId = getUserId(api, userChannel.getUserEmail());
        AlertsChannelConfiguration.AlertsChannelConfigurationBuilder builder = AlertsChannelConfiguration.builder();
        builder.userId(userId);
        return builder.build();
    }

    private static int getUserId(NewRelicApi api, String userEmail) {
        return api.getUsersApi().getByEmail(userEmail)
                .map(User::getId)
                .orElseThrow(() -> new NewRelicSyncException(format("NewRelic user with email %s does not exist", userEmail)));
    }
}
