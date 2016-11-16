package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import lombok.Getter;

import java.util.List;

@Getter
public class ChannelConfiguration {
    private final String policyName;
    private final List<Channel> channels;

    public ChannelConfiguration(String policyName, List<Channel> channels) {
        this.policyName = policyName;
        this.channels = channels;
    }
}
