package com.ocado.pandateam.newrelic.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ApplicationSettings {
    @JsonProperty("app_apdex_threshold")
    Float appApdexThreshold;
    @JsonProperty("end_user_apdex_threshold")
    Float endUserApdexThreshold;
    @JsonProperty("enable_real_user_monitoring")
    Boolean enableRealUserMonitoring;
    @JsonProperty("use_server_side_config")
    Boolean useServerSideConfig;
}
