package com.ocado.panda.newrelic.sync;

import com.ocado.newrelic.api.model.applications.Application;
import com.ocado.newrelic.api.model.applications.ApplicationSettings;
import com.ocado.panda.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.panda.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static java.lang.String.format;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ApplicationSynchronizer testee;
    private static final ApplicationConfiguration CONFIGURATION = createConfiguration();

    private static final String APPLICATION_NAME = "appName";
    private static final float USER_APDEX_THRESHOLD = 0.7f;
    private static final float APP_APDEX_THRESHOLD = 0.5f;
    private static final boolean ENABLE_REAL_USER_MONITORING = false;

    private static final Application APPLICATION = Application.builder().id(1).name(APPLICATION_NAME).build();

    @Before
    public void setUp() {
        testee = new ApplicationSynchronizer(apiMock);
    }

    @Test
    public void shouldThrowException_whenApplicationDoesNotExist() {
        // given
        when(applicationsApiMock.getByName(APPLICATION_NAME)).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);
        expectedException.expectMessage(format("Application %s does not exist", APPLICATION_NAME));

        // when
        testee.sync(CONFIGURATION);
    }

    @Test
    public void shouldUpdateApplication() {
        // given
        when(applicationsApiMock.getByName(APPLICATION_NAME)).thenReturn(Optional.of(APPLICATION));

        ApplicationSettings expectedSettings = ApplicationSettings.builder()
            .appApdexThreshold(APP_APDEX_THRESHOLD)
            .endUserApdexThreshold(USER_APDEX_THRESHOLD)
            .enableRealUserMonitoring(ENABLE_REAL_USER_MONITORING)
            .build();
        Application expectedApplicationUpdate = Application.builder()
            .name(APPLICATION_NAME)
            .settings(expectedSettings)
            .build();

        // when
        testee.sync(CONFIGURATION);

        // then
        verify(applicationsApiMock).update(APPLICATION.getId(), expectedApplicationUpdate);
    }

    private static ApplicationConfiguration createConfiguration() {
        return ApplicationConfiguration.builder()
            .applicationName(APPLICATION_NAME)
            .appApdexThreshold(APP_APDEX_THRESHOLD)
            .endUserApdexThreshold(USER_APDEX_THRESHOLD)
            .enableRealUserMonitoring(ENABLE_REAL_USER_MONITORING)
            .build();
    }
}
