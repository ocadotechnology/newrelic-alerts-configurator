package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.configuration.channel.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;

@Getter
@Builder
public class ChannelConfiguration {
    @NonNull
    private String policyName;
    @NonNull
    private Collection<Channel> channels;
}
