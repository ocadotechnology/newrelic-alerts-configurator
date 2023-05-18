package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import org.junit.Test;

import static com.ocadotechnology.newrelic.graphql.apiclient.internal.testutil.TestResources.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {
    @Test
    public void shouldReturnTrueIfErrorsPresent() {
        assertThat(result("/AlertPolicies_getById_response.json").hasErrors()).isFalse();
        assertThat(result("/AlertPolicies_getById_response.json").notFound()).isFalse();

        assertThat(result("/AlertPolicies_getById_accessDenied.json").hasErrors()).isTrue();
        assertThat(result("/AlertPolicies_getById_accessDenied.json").notFound()).isFalse();

        assertThat(result("/AlertPolicies_getById_notFound.json").hasErrors()).isTrue();
        assertThat(result("/AlertPolicies_getById_notFound.json").notFound()).isTrue();
    }

    private Result result(String fileName) {
        return fromJson(fileName, Result.class);
    }
}