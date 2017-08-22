# NewRelic Tools

This project contains following tools useful for developers that monitor their application using New Relic:
- [NewRelic API client](api-client/)
  Client for New Relic rest API
- [Alerts configurator](alerts-configurator/)
  Library that allows configure New Relic alerts from code. This is especially useful if you have multiple applications or instances which you want to monitor in similar way.

## Build and publish:

Set environment variables: `MAVEN_REPO_USERNAME`, `MAVEN_REPO_PASSWORD`.

Then execute:

```
./gradlew clean publish -Pbuild_version=<build_version>
```
If build_verison contains "-SNAPSHOT" suffix, it will be published to snapshot repository. Release repository will be used otherwise.
