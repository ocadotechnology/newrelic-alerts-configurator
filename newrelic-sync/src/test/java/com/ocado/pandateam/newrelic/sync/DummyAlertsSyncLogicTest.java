package com.ocado.pandateam.newrelic.sync;

import org.junit.Test;

public class DummyAlertsSyncLogicTest {

    @Test
    public void shouldSynchronizeAlerts() throws Exception {
        // given
        DummyAlertsSyncLogic logic = new DummyAlertsSyncLogic();

        // when
        logic.synchronizeAlerts();

        // then NO EXCEPTION
    }

}
