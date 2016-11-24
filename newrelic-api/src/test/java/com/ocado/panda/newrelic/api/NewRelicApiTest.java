package com.ocado.panda.newrelic.api;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.ocado.panda.newrelic.api.internal.NewRelicApiException;
import com.ocado.panda.newrelic.api.model.applications.Application;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.apache.http.HttpStatus.SC_OK;

public class NewRelicApiTest {

    @ClassRule
    public static final WireMockClassRule WIRE_MOCK = new WireMockClassRule(6766);

    private static final String APPLICATION_JSON = "application/json";

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private ApplicationsApi testee;

    private String applications;

    @Before
    public void setUp() throws IOException {
        WIRE_MOCK.resetMappings();
        testee = new NewRelicApi("http://localhost:6766", "secret").getApplicationsApi();
        applications = IOUtils.toString(NewRelicApiTest.class.getResource("/applications.json"), UTF_8);
    }

    @Test
    public void shouldGetApplicationCorrectly() throws IOException, NewRelicApiException {

        // given
        newRelicReturnsApplications();

        // when
        Optional<Application> app = testee.getByName("user_management");

        // then
        Assert.assertTrue(app.isPresent());
    }

    private void newRelicReturnsApplications() throws UnsupportedEncodingException {
        String queryParam = URLEncoder.encode("filter[name]", UTF_8.name());

        WIRE_MOCK.addStubMapping(
                get(urlPathEqualTo("/v2/applications.json"))
                        .withQueryParam(queryParam, equalTo("user_management"))
                        .withHeader("X-Api-Key", equalTo("secret"))
                        .willReturn(aResponse()
                                .withStatus(SC_OK)
                                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                                .withBody(applications)
                        ).build());
    }
}
