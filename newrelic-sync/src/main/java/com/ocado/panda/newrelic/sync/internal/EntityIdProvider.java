package com.ocado.panda.newrelic.sync.internal;

import com.ocado.panda.newrelic.api.NewRelicApi;
import com.ocado.panda.newrelic.api.model.applications.Application;
import com.ocado.panda.newrelic.api.model.transactions.KeyTransaction;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;
import lombok.AllArgsConstructor;

import java.util.Optional;

import static java.lang.String.format;

@AllArgsConstructor
public class EntityIdProvider {
    private NewRelicApi api;

    public Integer getApplicationId(String name) {
        Optional<Application> applicationOptional = api.getApplicationsApi().getByName(name);
        Application application = applicationOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Application %s does not exist", name)));
        return application.getId();
    }

    public Integer getKeyTransactionId(String name) {
        Optional<KeyTransaction> ktOptional = api.getKeyTransactionsApi().getByName(name);
        KeyTransaction kt = ktOptional.orElseThrow(
            () -> new NewRelicSyncException(format("Key transaction %s does not exist", name)));
        return kt.getId();
    }
}
