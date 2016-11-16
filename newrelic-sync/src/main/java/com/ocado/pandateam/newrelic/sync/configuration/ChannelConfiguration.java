package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChannelConfiguration {
    private String policyName;
    private List<Channel> channels;
}
