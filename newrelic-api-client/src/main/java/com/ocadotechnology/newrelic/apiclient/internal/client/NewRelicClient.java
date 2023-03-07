package com.ocadotechnology.newrelic.apiclient.internal.client;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;

public interface NewRelicClient {

    WebTarget target(String path);

    WebTarget target(Link link);
}
