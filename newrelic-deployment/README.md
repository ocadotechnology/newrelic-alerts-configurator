# NewRelic mark deployments
Application that can be used to mark deployments in NewRelic.
  
## Building
```../gradlew clean build```

Executable JAR is located in `build/libs/newrelic-deployment-{version}-executable.jar`

## Downloading from Maven repository

`curl -L -o newrelic-deployment.jar http://maven.ocado.com/nexus/service/local/artifact/maven/redirect?r=core-services-snapshots&g=com.ocado.panda.newrelic.alerts&a=newrelic-deployment&c=executable&v=LATEST`

## Usage
```java -jar newrelic-deployment.jar --help```

Should print:
```
Usage: NewRelicDeploymentCli [options]
  Options:
  * --action
       Action to perform
       Possible Values: [list, mark, remove]
  * --api-key
       NewRelic admin API key
  * --application
       Application name in NewRelic
    --changelog
       Deployment changelog - optional for 'mark' option
    --debug
       Debug mode
       Default: false
    --deploymentId
       Deployment Id to remove - required for 'remove' option
    --description
       Deployment description - optional for 'mark' option
    --help
       Display usage description
       Default: false
    --revision
       Deployment revision - required for 'mark' option
    --user
       Deployment user - optional for 'mark' option
```

## Listing deployments
```java -jar newrelic-deployment.jar --api-key=NEW_RELIC_API_KEY --action=list --application=APPLICATION_ID```

Required parameters:
* `--api-key`
* `--action`
* `--application`

Optional parameters:
* `--debug`

## Marking new deployment
```java -jar newrelic-deployment.jar --api-key=NEW_RELIC_ADMIN_API_KEY --action=mark --application=APPLICATION_ID --revision=REVISION_ID```

Required parameters:
* `--api-key`
* `--action`
* `--application`
* `--revision`

Optional parameters:
* `--debug`
* `--changelog`
* `--description`
* `--user`


## Removing deployment mark
```java -jar newrelic-deployment.jar --api-key=NEW_RELIC_ADMIN_API_KEY --action=remove --application=APPLICATION_ID --deploymentId=DEPLOYMENT_ID```

Required parameters:
* `--api-key`
* `--action`
* `--application`
* `--deploymentId`

Optional parameters:
* `--debug`
