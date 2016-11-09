package com.ocado.pandateam.newrelic.sync;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Configuration {

    private String applicationName;

    private String policyName;

    private double appAppdexThreshold;

    private double userAppdexThreshold;
}
