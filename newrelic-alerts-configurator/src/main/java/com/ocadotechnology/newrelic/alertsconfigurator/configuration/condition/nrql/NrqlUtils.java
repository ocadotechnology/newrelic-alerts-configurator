package com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.nrql;

import com.ocadotechnology.newrelic.apiclient.model.conditions.nrql.Nrql;

public final class NrqlUtils {

    private NrqlUtils() {
    }

    public static Nrql createNrql(NrqlConfiguration nrqlConfiguration) {
        return Nrql.builder()
                .query(nrqlConfiguration.getQuery())
                .sinceValue(String.valueOf(nrqlConfiguration.getSinceValue().getSince()))
                .build();
    }
}
