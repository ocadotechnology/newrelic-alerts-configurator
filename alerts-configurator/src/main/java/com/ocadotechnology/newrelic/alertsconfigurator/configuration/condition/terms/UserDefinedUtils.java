package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.terms;

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.UserDefinedConfiguration;
import com.ocadotechnology.newrelic.api.model.conditions.UserDefined;

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
