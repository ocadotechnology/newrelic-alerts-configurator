package com.ocado.pandateam.newrelic.sync.configuration.channel;

import com.ocado.pandateam.newrelic.api.model.channels.AlertsChannel;
import org.apache.commons.lang3.StringUtils;

public final class ChannelUtils {
    private ChannelUtils() {
    }

    public static boolean sameInstance(AlertsChannel alertChannel1, AlertsChannel alertChannel2) {
        return StringUtils.equals(alertChannel1.getName(), alertChannel2.getName())
                && StringUtils.equals(alertChannel1.getType(), alertChannel2.getType());
    }

    public static boolean same(AlertsChannel alertChannel1, AlertsChannel alertChannel2) {
        return sameInstance(alertChannel1, alertChannel2)
                && alertChannel1.getConfiguration().equals(alertChannel2.getConfiguration());
    }

}
