package com.ocado.pandateam.newrelic.sync.channel;

import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import com.ocado.pandateam.newrelic.api.model.AlertChannelConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SlackChannel implements Channel {
    private String channelName;
    private String slackUrl;
    private String slackChannel;

    @Override
    public AlertChannel getAsAlertChannel() {
        AlertChannelConfiguration config = AlertChannelConfiguration.builder()
                .url(slackUrl).channel(slackChannel).build();
        return AlertChannel.builder().name(channelName).type("slack").configuration(config).build();
    }
}
