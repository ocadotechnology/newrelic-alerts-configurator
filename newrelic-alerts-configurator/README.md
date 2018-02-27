# Table of contents

- [Configurator](#configurator)
- [Application configuration](#application-configuration)
- [Policy configuration](#policy-configuration)
    - [Alerts condition](#alerts-condition)
        - [APM application metric condition](#apm-application-metric-condition)
        - [APM JVM metric condition](#apm-jvm-metric-condition)
        - [APM key transaction metric condition](#apm-key-transaction-metric-condition)
        - [Server metric condition](#server-metric-condition)
        - [New Relic Browser metric condition](#new-relic-browser-metric-condition)
    - [Alerts external service condition](#alerts-external-service-condition)
        - [APM external service condition](#apm-external-service-condition)
    - [Alerts NRQL condition](#alerts-nrql-condition)
    - [User defined configuration](#user-defined-configuration)
    - [Term](#term)
    - [NRQL Term](#nrql-term)
    - [Notification channel](#notification-channel)
        - [Email channel](#email-channel)
        - [Slack channel](#slack-channel)
        - [Webhook channel](#webhook-channel)
        - [PagerDuty channel](#pagerduty-channel)
        - [NewRelic user channel](#newrelic-user-channel)
    - [Condition and Channel management policy](#condition-and-channel-management-policy)

## Configurator

`Configurator` is the main class used to synchronize all defined configurations with NewRelic. Example use of Configurator:

```java
Configurator configurator = new Configurator(yourApiKey);
configurator.setApplicationConfigurations(yourApplicationConfigurationsCollection);
configurator.setPolicyConfigurations(yourPolicyConfigurationsCollection);
configurator.sync();
```

Both `setApplicationConfigurations` and `setPolicyConfigurations` accept Java Collections of configurations.

## Application configuration

To configure your application in NewRelic you just need to create and pass your application configuration to NewRelic Alerts configurator.
Application configuration can be created using simple builder:

```java
ApplicationConfiguration.builder()
```

What you can set in your configuration:
- application name - Your application name.
- application apdex threshold (optional) - Set apdex threshold for application.
- end user apdex threshold (optional) - Set apdex threshold for end user.
- real user monitoring enabled (optional) - If real user monitoring is enabled. Default value is false.

If application with given application name does not exist exception will be thrown.

Example application configuration:

```java
ApplicationConfiguration configuration = ApplicationConfiguration.builder()
    .applicationName("Application name")
    .appApdexThreshold(0.5f)
    .endUserApdexThreshold(7.0f)
    .enableRealUserMonitoring(true)
    .build();
```

## Policy configuration

To configure your alerts policy in NewRelic you just need to create and pass your policy configuration to NewRelic Alerts configurator.
Policy configuration can be created using simple builder:

```java
PolicyConfiguration.builder()
```

What you can set in your configuration:
- policy name - Name of your alerts policy.
- incident preference - Rollup strategy options for your alerts policy. Possible values are:
    - Per policy
    - Per condition
    - Per condition and target
- conditions (optional) - Collection of [alerts conditions](#alerts-condition) which needs to be configured for your alerts policy.
- external service conditions (optional) - Collection of [alerts external service conditions](#alerts-external-service-condition)
  which needs to be configured for your alerts policy.
- nrql conditions (optional) - Collection of [alerts NRQL conditions](#alerts-nrql-condition) which needs to be configured for 
  your alerts policy.
- channels (optional) - Collection of [alerts channels](#notification-channel) which needs to be configured for your alerts policy.

If policy with given policy name exists - it will be updated.

If policy with given policy name does not exist - it will be created.

Example alerts policy configuration:

```java
PolicyConfiguration configuration = PolicyConfiguration.builder()
    .policyName("Policy name")
    .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
    .condition(apmAppCondition)
    .condition(apmKeyTransactionCondition)
    .externalServiceCondition(apmExternalServiceCondition)
    .nrqlCondition(nrqlCondition)
    .channel(emailChannel)
    .channel(slackChannel)
    .build()
```

### Alerts condition

Currently supported types of alerts policy conditions are:
- [APM application metric condition](#apm-application-metric-condition)
- [APM JVM metric condition](#apm-jvm-metric-condition)
- [APM key transaction metric condition](#apm-key-transaction-metric-condition)
- [Server metric condition](#server-metric-condition)
- [New Relic Browser metric condition](#new-relic-browser-metric-condition)

Condition creation/update/removal policy is described in [Condition and Channel management policy](#condition-and-channel-management-policy)

#### APM application metric condition

To create APM application metric condition for your alerts policy use simple builder:

```java
ApmAppCondition.builder()
```

What you can set for APM application metric condition:
- condition name - Name of your APM application metric condition.
- enabled (optional) - If your APM application metric condition is enabled. Default is true.
- applications - Collection of application names for which this condition is applied. If application with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - Apdex
    - Error percentage
    - Response time (web)
    - Response time (background)
    - Throughput (web)
    - Throughput (background)
    - User defined
- condition scope - Possible values are:
    -  Application - The average of all application instances' data is used during evaluation.
    -  Instance - Each application instance's data is evaluated individually
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.
- violation close timer - Duration (in hours) after which instance-based violations will automatically close.
  This field is only required when condition scope is set to 'Instance'. Possible values are:
    - 1
    - 2
    - 4
    - 8
    - 12
    - 24
- user defined configuration - configuration for [user defined metric](#user-defined-configuration).

Example APM application metric condition configuration:

```java
Condition apmAppCondition = ApmAppCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .application("Application name")
    .metric(ApmAppCondition.Metric.APDEX)
    .conditionScope(ApmAppCondition.ConditionScope.APPLICATION)
    .term(term)
    .build();
```

#### APM JVM metric condition

To create APM JVM metric condition for your alerts policy use simple builder:

```java
ApmJvmCondition.builder()
```

What you can set for APM JVM metric condition:
- condition name - Name of your APM application metric condition.
- enabled (optional) - If your APM application metric condition is enabled. Default is true.
- applications - Collection of application names for which this condition is applied. If application with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - Deadlocked threads
    - Heap memory usage
    - CPU utilization time
    - Garbage collection CPU time
- gc metric - Metric used to get GC CPU time. To use it metric has to be set to measure garbage collection CPU time. Possible values are:
    - PS MarkSweep
    - PS Scavenge   
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.
- violation close timer - Duration (in hours) after which instance-based violations will automatically close.
  Possible values are:
    - 1
    - 2
    - 4
    - 8
    - 12
    - 24

Example APM JVM metric condition configuration:

```java
Condition apmJvmCondition = ApmJvmCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .application("Application name")
    .metric(ApmJvmCondition.Metric.GC_CPU_TIME)
    .gcMetric(ApmJvmCondition.GcMetric.GC_MARK_SWEEP)
    .term(term)
    .violationCloseTimer(ViolationCloseTimer.DURATION_24)
    .build();
```

#### APM key transaction metric condition

To create APM key transaction metric condition for your alerts policy use simple builder:

```java
ApmKeyTransactionCondition.builder()
```

What you can set for APM key transaction metric condition:
- condition name - Name of your APM key transaction metric condition.
- enabled (optional) - If your APM key transaction metric condition is enabled. Default is true.
- keyTransactions - Collection of key transaction names for which this condition is applied. If key transaction with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - Apdex
    - Error percentage
    - Error count
    - Response time
    - Throughput
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.

Example APM key transaction metric condition configuration:

```java
Condition apmKeyTransactionCondition = ApmKeyTransactionCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .keyTransastion("Key Transaction name")
    .metric(ApmKeyTransactionCondition.Metric.APDEX)
    .term(term)
    .build();
```

**Note that key transaction cannot be created via New Relic API. Before configuration of condition, key transaction needs to be manually created in New Relic.**

#### Server metric condition

To create server metric condition for your alerts policy use simple builder:

```java
ServersMetricCondition.builder()
```

What you can set for server metric condition:
- condition name - Name of your server metric condition.
- enabled (optional) - If your server metric condition is enabled. Default is true.
- servers - Collection of server names for which this condition is applied. If server with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - CPU percentage
    - Disk I/O percentage
    - Memory percentage
    - Fullest disk percentage
    - Load average one minute
    - User defined
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.
- user defined configuration - configuration for [user defined metric](#user-defined-configuration).

Example server metric condition configuration:

```java
Condition serverMetricCondition = ServerMetricCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .server("some-host")
    .metric(ServersMetricCondition.Metric.CPU_PERCENTAGE)
    .term(term)
    .build();
```

#### New Relic Browser metric condition

To create New Relic Browser metric condition for your alerts policy use simple builder:

```java
BrowserCondition.builder()
```

What you can set for New Relic Browser metric condition:
- condition name - Name of your New Relic Browser metric condition
- enabled (optional) - If your New Relic Browser metric condition is enabled. Default is true.
- applications - Collection of application names for which this condition is applied. If application with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - End user Apdex
    - Total page load
    - Page rendering
    - Web application
    - Network
    - DOM processing
    - Request queuing
    - AJAX response time
    - Page views with JS errors
    - Page view throughput
    - AJAX throughput
    - User defined
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.
- user defined configuration - configuration for [user defined metric](#user-defined-configuration).

Example New Relic Browser metric condition configuration:

```java
Condition browserCondition = BrowserCondition.builder()
	.conditionName("Page views with JS errors")
	.enabled(true)
	.application("Application name")
	.metric(BrowserCondition.Metric.PAGE_VIEWS_WITH_JS_ERRORS)
	.term(term)
	.build();
```

**Please note New Relic Browser agent has to be installed.** 

### Alerts external service condition

Currently supported types of alerts policy external service conditions are:
- [APM external service condition](#apm-external-service-condition)

External Service Condition creation/update/removal policy is described in [Condition and Channel management policy](#condition-and-channel-management-policy)

#### APM external service condition

To create APM external service condition for your alerts policy use simple builder:

```java
ApmExternalServiceCondition.builder()
```

What you can set for APM external service condition:
- condition name - Name of your APM external service condition.
- enabled (optional) - If your APM external service condition is enabled. Default is true.
- applications - Collection of application names for which this condition is applied. If application with given name does not exist exception will be thrown.
- metric - Metric used in given condition. Possible values are:
    - Response time (average)
    - Response time (minimum)
    - Response time (maximum)
    - Throughput
- external service url - URL of the external service to be monitored. This string must not include the protocol.
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [terms](#term) used for alerts condition.

Example APM external service condition configuration:

```java
ExternalServiceCondition apmExternalServiceCondition = ApmExternalServiceCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .application("Application name")
    .externalServiceUrl("externalServiceUrl")
    .metric(ApmExternalServiceCondition.Metric.RESPONSE_TIME_AVERAGE)
    .term(term)
    .build();
```

### Alerts NRQL condition

NRQL Condition creation/update/removal policy is described in [Condition and Channel management policy](#condition-and-channel-management-policy)

To create NRQL condition for your alerts policy use simple builder:

```java
NrqlCondition.builder()
```

What you can set for NRQL condition:
- condition name - Name of your NRQL condition.
- enabled (optional) - If your NRQL condition is enabled. Default is true.
- run book url (optional) - The runbook URL to display in notifications.
- terms - Collection of [NRQL terms](#nrql-term) used for alerts condition.
- value function - How condition should be evaluated. Possible values are:
    - single_value - the condition will be evaluated by the raw value returned.
    - sum - the condition will evaluate on the sum of the query results.
- query - Query in NRQL.
- since value - The timeframe in which to evaulate the query. Possible values are:
    - 1
    - 2
    - 3
    - 4
    - 5

Example NRQL condition configuration:

```java
NrqlCondition.builder()
    .conditionName("Condition name")
    .enabled(true)
    .valueFunction(NrqlCondition.ValueFunction.SINGLE_VALUE)
    .term(term)
    .query("SELECT count(*) FROM `myApp:HealthStatus` WHERE healthy IS false")
    .sinceValue(NrqlCondition.SinceValue.SINCE_5)
    .build();
```

### User defined configuration

User defined configuration allows to use custom (user defined) metric in alert definition.

Example user defined configuration:

```java
UserDefinedConfiguration config = UserDefinedConfiguration.builder()
    .metric("MY_CUSTOM_METRIC")
    .valueFunction(UserDefinedConfiguration.ValueFunction.MAX)
    .build();
```

### Term

Terms are used both in alerts conditions and alerts external service conditions.
To create term configuration for condition use simple builder:

```java
TermsConfiguration.builder()
```

What you can set in term configuration:
- duration - Time (in minutes) for the condition to persist before triggering an event. Possible values are:
    - 5
    - 10
    - 15
    - 30
    - 60
    - 120
- operator - Determines what comparison will be used between the monitored value and the threshold term value to trigger an event. Possible values are:
    - Above
    - Below
    - Equal
- priority - Severity level for given term in condition. Possible values are:
    - Critical
    - Warning
- time function - Time function in which threshold term have to be reached in duration term to trigger an event. Possible values are:
    - All (for whole time)
    - Any (at least once in)
- threshold - Threshold that the monitored value must be compared to using the operator term for an event to be triggered.

Example term configuration:

```java
TermsConfiguration term = TermsConfiguration.builder()
    .durationTerm(DurationTerm.DURATION_5)
    .operatorTerm(OperatorTerm.BELOW)
    .thresholdTerm(0.8f)
    .priorityTerm(PriorityTerm.CRITICAL)
    .timeFunctionTerm(TimeFunctionTerm.ALL)
    .build();
```

### NRQL Term

NRQL Terms are used in NRQL conditions.
To create term configuration for condition use simple builder:

```java
NrqlTermsConfiguration.builder()
```

What you can set in term configuration:
- duration - Time (in minutes) for the condition to persist before triggering an event. Possible values are:
    - 1
    - 2
    - 3
    - 4
    - 5
    - 10
    - 15
    - 30
    - 60
    - 120
- operator - Determines what comparison will be used between the monitored value and the threshold term value to trigger an event. Possible values are:
    - Above
    - Below
    - Equal
- priority - Severity level for given term in condition. Possible values are:
    - Critical
    - Warning
- time function - Time function in which threshold term have to be reached in duration term to trigger an event. Possible values are:
    - All (for whole time)
    - Any (at least once in)
- threshold - Threshold that the monitored value must be compared to using the operator term for an event to be triggered.

Example term configuration:

```java
NrqlTermsConfiguration term = NrqlTermsConfiguration.builder()
    .durationTerm(NrqlDurationTerm.DURATION_1)
    .operatorTerm(OperatorTerm.BELOW)
    .thresholdTerm(0.8f)
    .priorityTerm(PriorityTerm.CRITICAL)
    .timeFunctionTerm(TimeFunctionTerm.ALL)
    .build();
```

### Notification channel

Currently supported types of alerts notification channels are:
- [Email channel](#email-channel)
- [Slack channel](#slack-channel)
- [Webhook channel](#webhook-channel)
- [PagerDuty channel](#pagerduty-channel)
- [NewRelic user channel](#newrelic-user-channel)

Channels creation/update/removal policy is described in [Condition and Channel management policy](#condition-and-channel-management-policy)
Only exception is NewRelic user channel - we will not delete it.

#### Email channel

To create email channel use simple builder:

```java
EmailChannel.builder()
```

What you can set in email channel configuration:
- channel name - Name of your alerts channel.
- email address - Email address to which alert event should be sent.
- include json attachment (optional) - If json attachment should be included in alert event email. Default value is false.

Example email channel configuration:

```java
Channel emailChannel = EmailChannel.builder()
    .channelName("Channel name")
    .emailAddress("your-email-address@ocado.com")
    .includeJsonAttachment(true)
    .build();
```

#### Slack channel

To create slack channel use simple builder:

```java
SlackChannel.builder()
```

What you can set in slack channel configuration:
- channel name - Name of your alerts channel.
- slack url - Slack url to your channel.
- team channel (optional) - Name of your team channel.

Example slack channel configuration:

```java
Channel slackChannel = SlackChannel.builder()
    .channelName("Channel name")
    .slackUrl("https://hooks.slack.com/services/...")
    .teamName("My team slack name")
    .build();
```

#### Webhook channel

To create webhook channel use simple builder:

```java
WebhookChannel.builder()
```

What you can set in webhook channel configuration:
- channel name - Name of your alerts channel.
- base URL - URL webhook address to which alert event should be sent.
- auth username - User name for BASIC authorization (optional)
- auth password - User password for BASIC authorization (optional)

Example webhook channel configuration:

```java
Channel webhookChannel = WebhookChannel.builder()
    .channelName("Channel name")
    .baseUrl("https://example.com/webhook/abcd123")
    .authUsername("john.doea")
    .authPassword("starwars1")
    .build();
```

#### PagerDuty channel

To create [PagerDuty](https://www.pagerduty.com/) channel use simple builder:

```java
PagerDutyChannel.builder()
```

What you can set in PagerDUty channel configuration:
- channel name - Name of your alerts channel.
- serviceKey - Service key integration ID defined in PagerDuty.

Example PagerDuty channel configuration:

```java
Channel pagerDutyChannel = PagerDutyChannel.builder()
    .channelName("Channel name")
    .serviceKey("1234-5678-ABCD-XYZ")
    .build();
```

#### NewRelic user channel

To create NewRelic user channel use simple builder:

```java
UserChannel.builder()
```

What you can set in NewRelic user channel:
- email address - NewRelic user's email address 

Example email channel configuration:

```java
Channel userChannel = UserChannel.builder()
    .userEmail("your-email-address@ocado.com")
    .build();
```

#### Condition and Channel management policy

All conditions and channels are managed in the same way.

1. If you don't define any conditions in the policy, synchronization process will not modify already existing conditions.
   Example:
   ```java
   PolicyConfiguration configuration = PolicyConfiguration.builder()
       .policyName("Policy name")
       .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
       .build()
   ```
   This configuration will not remove, create or update any existing condition.
   
1. If you define some conditions in the policy, then:
    - conditions which are defined in configuration, but are missing will be created.
    - conditions which are defined in configuration and already exist will be updated.
    - conditions which are not defined in configuration, but already exist will be removed.
   Example:
   ```java
   PolicyConfiguration configuration = PolicyConfiguration.builder()
       .policyName("Policy name")
       .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
       .condition(condition1)
       .condition(condition2)
       .build()
   ```
   This configuration will create/update condition1 and condition2, and will remove all other conditions.
   
1. If you define empty conditions list in the policy, then synchronization process will remove all existing conditions.
   Example:
   ```java
   PolicyConfiguration configuration = PolicyConfiguration.builder()
       .policyName("Policy name")
       .incidentPreference(PolicyConfiguration.IncidentPreference.PER_POLICY)
       .conditions(Collections.emptyList())
       .build()
   ```
   This configuration will not create nor update any conditions, but will remove all existing ones.
   