package com.ocado.pandateam.newrelic.sync;

import com.ocado.pandateam.newrelic.api.DummyApi;

/**
 * Dummy alerts sync logic.
 */
public class DummyAlertsSyncLogic {

    private final static DummyApi api = new DummyApi();

    /**
     * Synchronizes NewRelic alerts configuration.
     */
    public void synchronizeAlerts() {
        String message = api.getHelloWorldMessage();
        System.out.println(message);
    }

    public static void main(String[] args) {
        new DummyAlertsSyncLogic().synchronizeAlerts();
    }

}
