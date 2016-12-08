# NewRelic Alerts

NewRelic alert configuration library and API wrapper.

1. Build and publish:

    ```
    ./gradlew clean publish -Pbuild_version=<build_version>
    ```
    If build_verison contains "-SNAPSHOT" suffix, it will be published to snapshot repository. Release repository will be used otherwise.
    
See https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-sync for more details of how to use new relic alerts synchronizer.