# NewRelic Alerts

Contains:
- New Relic API wrapper
- Alerts Synchronizer
- Deployment marker

![](images/new_relic_library_diagram.png)
## New Relic API wrapper
See [newrelic-api/README.md](https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-api) for more details of New Relic API wrapper.

## Alerts Synchronizer
    
See [newrelic-sync/README.md](https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-sync) for more details of how to use New Relic alerts synchronizer.

## Mark deployments

See [newrelic-deployments/README.md](https://gitlab.tech.lastmile.com/internal-open-source/newrelic-alerts/tree/master/newrelic-deployment) for more details of how to mark deployments.


## Build and publish:

```
./gradlew clean publish -Pbuild_version=<build_version>
```
If build_verison contains "-SNAPSHOT" suffix, it will be published to snapshot repository. Release repository will be used otherwise.
