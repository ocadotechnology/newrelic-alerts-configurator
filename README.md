# NewRelic Tools

Contains:
- NewRelic API client
- Alerts configuration uploader
- Deployment marker

![](images/new_relic_library_diagram.png)
## NewRelic API client
See [api-client/README.md](api-client/) for more details of New Relic API client.

## Alerts Synchronizer
    
See [alert-config-uploader/README.md](alert-config-uploader/) for more details of how to use New Relic alerts configuration uploader.

## Mark deployments

See [deployment-marker/README.md](deployment-marker/) for more details of how to mark deployments.


## Build and publish:

Set environment variables: `MAVEN_REPO_USERNAME`, `MAVEN_REPO_PASSWORD`.

Then execute:

```
./gradlew clean publish -Pbuild_version=<build_version>
```
If build_verison contains "-SNAPSHOT" suffix, it will be published to snapshot repository. Release repository will be used otherwise.
