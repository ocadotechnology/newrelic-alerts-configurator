package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import com.ocadotechnology.newrelic.apiclient.model.conditions.Nrql;

public final class NrqlUtils {

    private NrqlUtils() {
    }

    public static Nrql mapNrql(NrqlConfiguration nrqlConfiguration) {
        return Nrql.builder()
                .query(nrqlConfiguration.getQuery())
                .since_value(nrqlConfiguration.getSinceValue().getSince())
            .build();
    }
}
