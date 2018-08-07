package com.ocadotechnology.newrelic.apiclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.ocadotechnology.newrelic.apiclient.model.applications.Application;
import com.ocadotechnology.newrelic.apiclient.model.channels.AlertsChannel;

public class NewRelicApiIntegrationTest {
    @ClassRule
    public static final WireMockClassRule WIRE_MOCK = new WireMockClassRule(6766);

    private static final String APPLICATION_JSON = "application/json";
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private NewRelicApi testee;
    private String applications;
    private String channels1;
    private String channels2;

    @Before
    public void setUp() throws IOException {
        WIRE_MOCK.resetMappings();
        testee = new NewRelicApi("http://localhost:6766", "http://localhost:6766", "secret");
        applications = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/applications.json"), UTF_8);
        channels1 = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/channels1.json"), UTF_8);
        channels2 = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/channels2.json"), UTF_8);
    }

    @Test
    public void shouldGetApplicationByNameCorrectly() throws IOException {

        // given
        newRelicReturnsApplications();

        // when
        Optional<Application> app = testee.getApplicationsApi().getByName("application_name");

        // then
        assertThat(app).isNotEmpty();
    }

    @Test
    public void shouldGetPaginatedChannelsCorrectly() {

        // given
        newRelicReturnsPaginatedChannels();

        // when
        List<AlertsChannel> channels = testee.getAlertsChannelsApi().list();

        // then
        Set<Integer> channelsIds = channels.stream().map(AlertsChannel::getId).collect(Collectors.toSet());
        assertThat(channelsIds).containsExactlyInAnyOrder(1, 2);
    }

    private void newRelicReturnsApplications() throws UnsupportedEncodingException {
        String queryParam = URLEncoder.encode("filter[name]", UTF_8.name());

        WIRE_MOCK.addStubMapping(
                get(urlPathEqualTo("/v2/applications.json"))
                        .withQueryParam(queryParam, equalTo("application_name"))
                        .withHeader("X-Api-Key", equalTo("secret"))
                        .willReturn(aResponse()
                                .withStatus(SC_OK)
                                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                                .withBody(applications)
                        ).build());
    }

    private void newRelicReturnsPaginatedChannels() {
        WIRE_MOCK.addStubMapping(
                get(urlPathEqualTo("/v2/alerts_channels.json"))
                        .withHeader("X-Api-Key", equalTo("secret"))
                        .willReturn(aResponse()
                                .withStatus(SC_OK)
                                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                                .withHeader("Link",
                                        "<http://localhost:6766/v2/alerts_channels.json?page=2>; rel=\"next\", " +
                                                "<http://localhost:6766/v2/alerts_channels.json?page=2>; rel=\"last\"")
                                .withBody(channels1)
                        ).build());

        WIRE_MOCK.addStubMapping(
                get(urlPathEqualTo("/v2/alerts_channels.json"))
                        .withQueryParam("page", equalTo(String.valueOf(2)))
                        .withHeader("X-Api-Key", equalTo("secret"))
                        .willReturn(aResponse()
                                .withStatus(SC_OK)
                                .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                                .withHeader("Link",
                                        "<http://localhost:6766/v2/alerts_channels.json?page=2>; rel=\"last\"")
                                .withBody(channels2)
                        ).build());
    }
}
