package com.ocadotechnology.newrelic.alertsconfigurator;

import com.google.common.collect.ImmutableList;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.DashboardConfiguration;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardEditable;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardIcon;
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardVisibility;
import com.ocadotechnology.newrelic.alertsconfigurator.exception.NewRelicSyncException;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.Dashboard;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.dashboard.DashboardMetadata;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.Widget;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.WidgetData;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.WidgetLayout;
import com.ocadotechnology.newrelic.apiclient.model.dashboards.widget.WidgetPresentation;
import org.junit.Before;
import org.junit.Test;

import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardEditable.EDITABLE_BY_OWNER;
import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardIcon.BELL;
import static com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard.DashboardVisibility.ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DashboardConfiguratorTest extends AbstractConfiguratorTest {

    private static final DashboardConfiguration CONFIGURATION = createConfiguration();

    private static final String DASHBOARD_TITLE = "Dashboard title";

    private static final String OWNER_EMAIL = "some@test.email";

    private static final String DASHBOARD_DESCRIPTION = "Details of app 2";

    private static final DashboardEditable DASHBOARD_EDITABILITY = EDITABLE_BY_OWNER;

    private static final DashboardVisibility DASHBOARD_VISIBILITY = ALL;

    private static final DashboardIcon DASHBOARD_ICON = BELL;

    private static final ImmutableList<Widget> DASHBOARD_WIDGETS = ImmutableList.of(errorCountWidget());

    private static final Dashboard CREATED_DASHBOARD = Dashboard.builder()
            .title(DASHBOARD_TITLE)
            .ownerEmail(OWNER_EMAIL)
            .description(DASHBOARD_DESCRIPTION)
            .editable(DASHBOARD_EDITABILITY.getEditable())
            .visibility(DASHBOARD_VISIBILITY.getVisibility())
            .apiUrl("http://some.api.url")
            .uiUrl("https://that.is.secure.url")
            .icon(DASHBOARD_ICON.getIcon())
            .metadata(DashboardMetadata.builder().version(1).build())
            .widgets(DASHBOARD_WIDGETS)
            .build();

    private static final Dashboard UPDATED_DASHBOARD = CREATED_DASHBOARD.toBuilder().id(123).build();

    private DashboardConfigurator testee;

    @Before
    public void setUp() {
        testee = new DashboardConfigurator(apiMock);
    }

    @Test
    public void shouldThrowException_whenMoreThanOneDashboardWithGivenTitleExist() {
        // given
        Dashboard dashboard2 = Dashboard.builder().build();
        when(dashboardsApiMock.getByTitle(anyString())).thenReturn(ImmutableList.of(CREATED_DASHBOARD, dashboard2));

        // when
        Throwable exception = catchThrowable(() -> testee.sync(CONFIGURATION));

        // then - exception
        assertThat(exception).isInstanceOf(NewRelicSyncException.class);
        assertThat(exception).hasMessage("There are more than one dashboards matching the title Dashboard title");
    }

    @Test
    public void shouldThrowException_whenNoDashboardWithGivenTitleExist() {
        // given
        when(dashboardsApiMock.getByTitle(anyString())).thenReturn(ImmutableList.of());

        // when
        Throwable exception = catchThrowable(() -> testee.sync(CONFIGURATION));

        // then - exception
        assertThat(exception).isInstanceOf(NewRelicSyncException.class);
        assertThat(exception).hasMessage("Unable to find dashboard with title 'Dashboard title' for update");
    }

    @Test
    public void shouldUpdateDashboard() {
        // given
        when(dashboardsApiMock.getByTitle(anyString())).thenReturn(ImmutableList.of(UPDATED_DASHBOARD));
        when(dashboardsApiMock.getById(UPDATED_DASHBOARD.getId())).thenReturn(UPDATED_DASHBOARD);

        // when
        testee.sync(CONFIGURATION);

        // then
        verify(dashboardsApiMock).update(UPDATED_DASHBOARD);
    }

    private static DashboardConfiguration createConfiguration() {
        return DashboardConfiguration.builder()
                .title(DASHBOARD_TITLE)
                .description(DASHBOARD_DESCRIPTION)
                .icon(DASHBOARD_ICON)
                .visibility(DASHBOARD_VISIBILITY)
                .editable(DASHBOARD_EDITABILITY)
                .ownerEmail(OWNER_EMAIL)
                .widgets(DASHBOARD_WIDGETS)
                .build();
    }

    private static Widget errorCountWidget() {
        WidgetPresentation presentation = WidgetPresentation.builder()
                .title("Widget title")
                .notes("Widget notes")
                .build();

        WidgetLayout layout = WidgetLayout.builder()
                .column(0)
                .row(0)
                .width(3)
                .height(2)
                .build();

        WidgetData nrql = WidgetData.builder()
                .nrql("SELECT count(*) FROM ErrorTransaction WHERE error IS true")
                .build();

        return Widget.builder()
                .presentation(presentation)
                .layout(layout)
                .visualization("gauge")
                .data(ImmutableList.of(nrql))
                .build();
    }
}
