package com.ocado.pandateam.newrelic.sync.configuration;

import com.ocado.pandateam.newrelic.sync.channel.Channel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChannelConfiguration {
    List<Channel> channels;
}
