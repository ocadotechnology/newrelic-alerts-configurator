package com.ocado.panda.newrelic.api.model.applications;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
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
