# NewRelic Alerts

NewRelic alert configuration library and API wrapper.

## Alerts Synchronizer
    
See [newrelic-sync/README.md](https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-sync) for more details of how to use new relic alerts synchronizer.

## Mark deployments

See [newrelic-deployments/README.md](https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-deployment) for more details of how to mark deployments.

## Build and publish:

    ```
    ./gradlew clean publish -Pbuild_version=<build_version>
    ```
    If build_verison contains "-SNAPSHOT" suffix, it will be published to snapshot repository. Release repository will be used otherwise.
