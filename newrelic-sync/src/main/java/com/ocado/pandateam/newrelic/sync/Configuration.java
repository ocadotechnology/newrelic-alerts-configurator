package com.ocado.pandateam.newrelic.sync;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Configuration {

    private String applicationName;

    private String policyName;

    private float appApdexThreshold;

    private float userApdexThreshold;

    private List<EmailChannel> emailChannels;
}
