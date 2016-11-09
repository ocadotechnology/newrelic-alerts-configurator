package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ApplicationSummary {
    @JsonProperty("response_time")
    Float responseTime;
    @JsonProperty
    Float throughput;
    @JsonProperty("error_rate")
    Float errorRate;
    @JsonProperty("apdex_target")
    Float apdexTarget;
    @JsonProperty("apdex_score")
    Float apdexScore;
    @JsonProperty("host_count")
    Integer hostCount;
    @JsonProperty("instance_count")
    Integer instanceCount;
    @JsonProperty("concurrent_instance_count")
    Integer concurrentInstanceCount;
}
