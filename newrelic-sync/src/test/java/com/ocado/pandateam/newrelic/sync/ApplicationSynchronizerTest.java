package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.sync.configuration.ApplicationConfiguration;
import com.ocado.pandateam.newrelic.sync.exception.NewRelicSyncException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationSynchronizerTest extends AbstractSynchronizerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ApplicationConfiguration applicationConfigurationMock;

    private ApplicationSynchronizer testee;

    private static final String APPLICATION_NAME = "appName";
    private static final float USER_APDEX_THRESHOLD = 0.7f;
    private static final float APP_APDEX_THRESHOLD = 0.5f;

    @Before
    public void setUp() {
        testee = new ApplicationSynchronizer(apiMock, applicationConfigurationMock);
        mockValidApplicationConfigurationMock();
    }

    @Test
    public void shouldThrowException_whenApplicationDoesNotExist() throws NewRelicSyncException {
        // given
        when(applicationsApiMock.getByName(eq(APPLICATION_NAME))).thenReturn(Optional.empty());

        // then - exception
        expectedException.expect(NewRelicSyncException.class);

        // when
        testee.sync();
    }

    private void mockValidApplicationConfigurationMock() {
        when(applicationConfigurationMock.getApplicationName()).thenReturn(APPLICATION_NAME);
        when(applicationConfigurationMock.getUserApdexThreshold()).thenReturn(USER_APDEX_THRESHOLD);
        when(applicationConfigurationMock.getAppApdexThreshold()).thenReturn(APP_APDEX_THRESHOLD);
    }
}
