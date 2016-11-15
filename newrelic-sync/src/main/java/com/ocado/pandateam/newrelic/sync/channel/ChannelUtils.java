package com.ocado.pandateam.newrelic.sync.channel;

import com.ocado.pandateam.newrelic.api.model.AlertChannel;
import org.apache.commons.lang3.StringUtils;

public final class ChannelUtils {
    private ChannelUtils() {
    }

    public static boolean sameInstance(AlertChannel alertChannel1, AlertChannel alertChannel2) {
        return StringUtils.equals(alertChannel1.getName(), alertChannel2.getName())
                && StringUtils.equals(alertChannel1.getType(), alertChannel2.getType());
    }

    public static boolean same(AlertChannel alertChannel1, AlertChannel alertChannel2) {
        return sameInstance(alertChannel1, alertChannel2)
                && alertChannel1.getConfiguration().equals(alertChannel2.getConfiguration());
    }

}
