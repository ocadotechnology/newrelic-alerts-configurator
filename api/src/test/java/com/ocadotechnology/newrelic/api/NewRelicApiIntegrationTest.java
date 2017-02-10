package com.ocadotechnology.newrelic.api;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.ocadotechnology.newrelic.api.model.applications.Application;
import com.ocadotechnology.newrelic.api.model.channels.AlertsChannel;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.http.HttpStatus.SC_OK;

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
        testee = new NewRelicApi("http://localhost:6766", "secret");
        applications = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/applications.json"), UTF_8);
        channels1 = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/channels1.json"), UTF_8);
        channels2 = IOUtils.toString(NewRelicApiIntegrationTest.class.getResource("/channels2.json"), UTF_8);

    }

    @Test
    public void shouldGetApplicationByNameCorrectly() throws IOException {

        // given
        newRelicReturnsApplications();

        // when
        Optional<Application> app = testee.getApplicationsApi().getByName("user_management");

        // then
        Assert.assertTrue(app.isPresent());
    }

    @Test
    public void shouldGetPaginatedChannelsCorrectly() throws IOException {

        // given
        newRelicReturnsPaginatedChannels();

        // when
        List<AlertsChannel> channels = testee.getAlertsChannelsApi().list();
        Set<Integer> channelsIds = channels.stream().map(AlertsChannel::getId).collect(Collectors.toSet());

        // then
        Assert.assertEquals(2, channels.size());
        Assert.assertTrue(channelsIds.contains(1));
        Assert.assertTrue(channelsIds.contains(2));
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

    private void newRelicReturnsPaginatedChannels() throws UnsupportedEncodingException {
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
