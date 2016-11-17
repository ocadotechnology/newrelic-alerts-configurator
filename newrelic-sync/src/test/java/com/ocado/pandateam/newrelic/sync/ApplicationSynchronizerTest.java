package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.model.applications.Application;
import com.ocado.pandateam.newrelic.api.model.applications.Settings;
import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ApplicationSynchronizer testee;
    private ApplicationConfiguration applicationConfiguration = createConfiguration();

    private static final String APPLICATION_NAME = "appName";
    private static final float USER_APDEX_THRESHOLD = 0.7f;
    private static final float APP_APDEX_THRESHOLD = 0.5f;
    private static final boolean ENABLE_REAL_USER_MONITORING = false;

    private static final Application APPLICATION = Application.builder().id(1).name(APPLICATION_NAME).build();

    @Before
    public void setUp() {
        testee = new ApplicationSynchronizer(apiMock, applicationConfiguration);
    }

    @Test
    public void shouldThrowException_whenApplicationDoesNotExist() throws NewRelicSyncException {
        // given
        when(applicationsApiMock.getByName(eq(APPLICATION_NAME))).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Application %s does not exist", APPLICATION_NAME));

        // when
        testee.sync();
    }

    @Test
    public void shouldUpdateApplication() throws NewRelicSyncException {
        // given
        when(applicationsApiMock.getByName(eq(APPLICATION_NAME))).thenReturn(Optional.of(APPLICATION));

        Settings expectedSettings = Settings.builder()
            .appApdexThreshold(APP_APDEX_THRESHOLD)
            .endUserApdexThreshold(USER_APDEX_THRESHOLD)
            .enableRealUserMonitoring(ENABLE_REAL_USER_MONITORING)
            .build();
        Application expectedApplicationUpdate = Application.builder()
            .name(APPLICATION_NAME)
            .settings(expectedSettings)
            .build();

        // when
        testee.sync();

        // then
        verify(applicationsApiMock).update(eq(APPLICATION.getId()), eq(expectedApplicationUpdate));
    }

    private ApplicationConfiguration createConfiguration() {
        return ApplicationConfiguration.builder()
            .applicationName(APPLICATION_NAME)
            .appApdexThreshold(APP_APDEX_THRESHOLD)
            .endUserApdexThreshold(USER_APDEX_THRESHOLD)
            .enableRealUserMonitoring(ENABLE_REAL_USER_MONITORING)
            .build();
    }
}
