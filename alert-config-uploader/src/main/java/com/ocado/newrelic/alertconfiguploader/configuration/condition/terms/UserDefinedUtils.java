package com.ocado.newrelic.alertconfiguploader.configuration.condition.terms;

import com.ocado.newrelic.alertconfiguploader.configuration.condition.UserDefinedConfiguration;
import com.ocado.newrelic.api.model.conditions.UserDefined;

public final class UserDefinedUtils {

    private UserDefinedUtils() {

    }

    public static UserDefined createUserDefined(UserDefinedConfiguration userDefinedConfiguration) {
        if(userDefinedConfiguration != null) {
            return UserDefined.builder()
                    .metric(userDefinedConfiguration.getMetric())
                    .valueFunction(userDefinedConfiguration.getValueFunction().getAsString())
                    .build();
        }
        return null;
    }
}
