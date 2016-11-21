package com.ocado.pandateam.newrelic.api.model.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * See <a href="https://rpm.newrelic.com/api/explore/alerts_channels/list">Doc</a>
 */
@Value
@Builder
@AllArgsConstructor
public class AlertsChannel {
    @JsonProperty
    Integer id;
    @JsonProperty
    String name;
    @JsonProperty
    String type;
    @JsonProperty
    AlertsChannelConfiguration configuration;
    @JsonProperty
    AlertsChannelLinks links;

    public boolean sameInstance(AlertsChannel other) {
        return StringUtils.equals(this.getName(), other.getName())
            && StringUtils.equals(this.getType(), other.getType());
    }

    public boolean same(AlertsChannel other) {
        return sameInstance(other) && Objects.equals(this.getConfiguration(), other.getConfiguration());
    }
}
