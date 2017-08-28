package com.ocadotechnology.newrelic.apiclient.internal;

import com.ocadotechnology.newrelic.apiclient.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.apiclient.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.apiclient.internal.model.AlertsPolicyList;
import com.ocadotechnology.newrelic.apiclient.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultAlertsPoliciesApiTest {
    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private Response responseMock;
    private AlertsPoliciesApi testee;

    @Before
    public void setUp() {
        NewRelicClient clientMock = mock(NewRelicClient.class);
        WebTarget webTargetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);

        when(clientMock.target("/v2/alerts_policies.json")).thenReturn(webTargetMock);
        when(webTargetMock.queryParam("filter[name]", "policy")).thenReturn(webTargetMock);
        when(webTargetMock.request(APPLICATION_JSON_TYPE)).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);

        testee = new DefaultAlertsPoliciesApi(clientMock);
    }

    @Test
    public void getByName_shouldReturnPolicy_whenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(asList(
                AlertsPolicy.builder().name("policy").build(),
                AlertsPolicy.builder().name("policy1").build()
        )));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertThat(policyOptional).isNotEmpty();
    }

    @Test
    public void getByName_shouldNotReturnPolicy_whenClientReturnsNotMatchingResult() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(Collections.singletonList(
                AlertsPolicy.builder().name("policy1").build()
        )));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertThat(policyOptional).isEmpty();
    }

    @Test
    public void getByName_shouldNotReturnPolicy_whenClientReturnsEmptyList() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(Collections.emptyList()));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertThat(policyOptional).isEmpty();
    }
}
