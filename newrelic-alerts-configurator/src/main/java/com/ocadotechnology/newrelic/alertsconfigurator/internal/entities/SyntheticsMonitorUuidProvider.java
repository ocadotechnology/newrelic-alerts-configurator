package com.ocadotechnology.newrelic.alertsconfigurator.internal.entities;

import static java.lang.String.format;

import java.util.Optional;

import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.NewRelicApi;
import com.ocadotechnology.newrelic.apiclient.model.synthetics.Monitor;

class SyntheticsMonitorUuidProvider implements UuidProvider {

    @Override
    public String getUuid(NewRelicApi api, String name) {
        Optional<Monitor> monitorOptional = api.getSyntheticsMonitorsApi().getByName(name);
        Monitor monitor = monitorOptional.orElseThrow(
                () -> new NewRelicSyncException(format("Synthetics Monitor %s does not exist", name)));
        return monitor.getUuid();
    }
}
