# NewRelic Alerts Configurator

NewRelic Alerts Configurator can be used to configure NewRelic alerts for your application. Instead of defining alerts 
through UI you can define them in code.
It allows you to automatize alerts configuration, easily recover them in case of wipe out and have full history of changes in 
your version control system.

## How to use NewRelic Alerts Configurator

First step is to obtain API key for given NewRelic account. Then You can create configuration. For example:

```java
// Create alert condition
TermsConfiguration above90Percent = TermsConfiguration.builder()
        .operatorTerm(OperatorTerm.ABOVE)
        .thresholdTerm(90f)
        .durationTerm(DurationTerm.DURATION_5)
        .timeFunctionTerm(TimeFunctionTerm.ALL)
        .priorityTerm(PriorityTerm.CRITICAL)
        .build();

Condition cpuCondition = ServersMetricCondition.builder()
        .conditionName("CPU Usage")
        .enabled(true)
        .server("some-host")
        .metric(ServersMetricCondition.Metric.CPU_PERCENTAGE)
        .term(above90Percent)
        .build();

// Create email channel for notifications
Channel emailChannel = EmailChannel.builder()
        .channelName("Team email")
        .emailAddress("my-team@my-company.com")
        .build();

// Create policy
PolicyConfiguration policy = PolicyConfiguration.builder()
        .policyName("My application policy")
        .condition(cpuCondition)
        .channel(emailChannel)
        .build();

// Synchronize changes
Configurator configurator = new Configurator("MY_REST_API_KEY");
configurator.setPolicyConfigurations(Collections.singletonList(policy));
configurator.sync();
```

That's all!

This code creates alert policy that will raise critical alert whenever some-host's cpu usage will raise above 90% in last 5
 minutes. Information about alert will be emailed to my-team@my-company.com
 
More examples can be found in [newrelic-alerts-configurator-examples](newrelic-alerts-configurator-examples) module.

### Experimental configurator
NewRelic actively deprecates parts of it's REST API encouraging a shift towards GraphQL API. 
There is therefore an alternative configurator that uses [NewRelic GraphQL API](https://docs.newrelic.com/docs/apis/nerdgraph/get-started/introduction-new-relic-nerdgraph/) where possible.
At this point it's experimental.
```java
NewRelicApi api = new NewRelicHybridApi("MY_REST_API_KEY", MY_ACCOUNT_ID);
Configurator configurator = new Configurator(api);
```
 
## How to obtain New Relic REST API key and account ID

In above examples we used **MY_REST_API_KEY** and **MY_ACCOUNT_ID**.  
Details on how to obtain API key can be found in
[NewRelic's REST API docs](https://docs.newrelic.com/docs/apis/rest-api-v2/getting-started/api-keys)  
Details on how to obtain account ID can be found in
[NewRelic's account docs](https://docs.newrelic.com/docs/accounts/accounts-billing/account-structure/account-id/)

**Note that for some configurations you will need Admin User's New Relic API key!**

## Binaries

Library is available in Maven Central
```xml
<dependency>
    <groupId>com.ocadotechnology.newrelicalertsconfigurator</groupId>
    <artifactId>newrelic-alerts-configurator</artifactId>
    <version>5.0.1</version>
</dependency>
```

## Features

Detailed list of supported features can be found in [newrelic-alerts-configurator](newrelic-alerts-configurator/) module.

## NewRelic API Client

This project contains following library useful for developers that monitor their application using New Relic:
- [NewRelic API client](newrelic-api-client/)
  Client for New Relic rest API

## Testing

To run tests call:

```bash
./gradlew test
```

## Build

To build call:

```bash
./gradlew clean build
```

## Contributing

We encourage you to contribute to NewRelic Alerts Configurator. Please check out the [Contributing guide](CONTRIBUTING.md) for 
guidelines about how to proceed.

## License

This product is licensed under Apache License 2.0. For details see [license file](LICENSE)