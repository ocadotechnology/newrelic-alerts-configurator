package com.ocadotechnology.newrelic.api.internal;

import com.ocadotechnology.newrelic.api.AlertsPoliciesApi;
import com.ocadotechnology.newrelic.api.internal.DefaultAlertsPoliciesApi;
import com.ocadotechnology.newrelic.api.internal.client.NewRelicClient;
import com.ocadotechnology.newrelic.api.internal.model.AlertsPolicyList;
import com.ocadotechnology.newrelic.api.model.policies.AlertsPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAlertsPoliciesApiTest {
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
    public void shouldReturnPolicyWhenClientReturnsNotUniqueResult() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(asList(
                AlertsPolicy.builder().name("policy").build(),
                AlertsPolicy.builder().name("policy1").build()
        )));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertTrue(policyOptional.isPresent());
    }

    @Test
    public void shouldNotReturnPolicyWhenClientReturnsNotMatchingResult() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(Collections.singletonList(
                AlertsPolicy.builder().name("policy1").build()
        )));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertFalse(policyOptional.isPresent());
    }

    @Test
    public void shouldNotReturnPolicyWhenClientReturnsEmptyList() throws Exception {

        // given
        when(responseMock.readEntity(AlertsPolicyList.class)).thenReturn(new AlertsPolicyList(Collections.emptyList()));

        // when
        Optional<AlertsPolicy> policyOptional = testee.getByName("policy");

        // then
        assertFalse(policyOptional.isPresent());
    }
}
