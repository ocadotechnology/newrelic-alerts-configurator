# Configurator DSL
To create and run [`Configurator`](../newrelic-alerts-configurator#configurator) use `configurator` dsl. 

```kotlin
configurator(yourApiKey) {
    applications {
        application { /* configuration described in Application configuration DSL section */ }
        application { /* configuration described in Application configuration DSL section */ }
    }
    policies {
        policy { /* configuration described in Policy configuration DSL section */ }
        policy { /* configuration described in Policy configuration DSL section */ }
        policy { /* configuration described in Policy configuration DSL section */ }
    }
}
```

This will create `Configurator`, set applications and policies, and run synchronization process

# Application configuration DSL
To create [`ApplicationConfiguration`](../newrelic-alerts-configurator#application-configuration) use `applicationConfiguration` dsl.

```kotlin
val config: ApplicationConfiguration = applicationConfiguration {
    applicationName = "test"
    appApdexThreshold = 0.5f
    endUserApdexThreshold = 0.7f
    enableRealUserMonitoring = true
}
```

# Policy configuration DSL
To create [`PolicyConfiguration`](../newrelic-alerts-configurator#policy-configuration) use `policyConfiguration` dsl.

```kotlin
val config: PolicyConfiguration = policyConfiguration {
    policyName = "Application Policy"
    incidentPreference = PolicyConfiguration.IncidentPreference.PER_POLICY

    conditions {
        nrql { /* configuration described in NRQL condition DSL section */ }
        apmApp { /* configuration described in APM application metric condition DSL section */ }
        apmJvm { /* configuration described in Apm JVM condition DSL section */ }
        browser { /* configuration described in New Relic browser metric condition DSL section */ }
        synthetics { /* configuration described in Synthetics Monitor Failure condition DSL section */ }
        serversMetric { /* configuration described in Servers metric condition DSL section */ }
        apmKeyTransation { /* configuration described in Apm key transaction condition DSL section */ }
        apmExternalService { /* configuration described in APM external service condition DSL section */ }
    }

    channels {
        user { /* configuration described in User channel DSL section */ }
        email { /* configuration described in Email channel DSL section */ }
        slack { /* configuration described in Slack channel DSL section */ }
        webhook { /* configuration described in Webhook channel DSL section */ }
        pagerDuty { /* configuration described in PagerDuty channel DSL section */ }
    }
}
```

# APM application metric condition DSL
To create [`ApmAppCondition`](../newrelic-alerts-configurator#apm-application-metric-condition) use `apmAppCondition` dsl.

```kotlin
val config: ApmAppCondition = apmAppCondition {
    conditionName = "Condition name"
    enabled = true
    applications = listOf("app1", "app2")
    metric = ApmAppCondition.Metric.APDEX
    conditionScope = ApmAppCondition.ConditionScope.APPLICATION
    runBookUrl = "runBookUrl"
    violationCloseTimer = ViolationCloseTimer.DURATION_24
    
    userDefined { /* configuration described in User defined configuration DSL section */ }
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# APM JVM metric condition DSL
To create [`ApmJVMCondition`](../newrelic-alerts-configurator#apm-jvm-metric-condition) use `apmJvmCondition` dsl.

```kotlin
val config: ApmJVMCondition = apmJvmCondition {
    conditionName = "Condition name"
    enabled = true
    applications = listOf("app1", "app2")
    metric = ApmJvmCondition.Metric.GC_CPU_TIME
    gcMetric = ApmJvmCondition.GcMetric.GC_MARK_SWEEP
    runBookUrl = "runBookUrl"
    violationCloseTimer = ViolationCloseTimer.DURATION_24
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# APM key transaction metric condition DSL
To create [`ApmKeyTransactionCondition`](../newrelic-alerts-configurator#apm-key-transaction-metric-condition) use `apmKeyTransactionCondition` dsl.

```kotlin
val config: ApmKeyTransactionCondition = apmKeyTransactionCondition {
    conditionName = "Condition name"
    enabled = true
    keyTransaction = "Key Transaction name"
    metric = ApmKeyTransactionCondition.Metric.APDEX
    runBookUrl = "runBookUrl"
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# Server metric condition DSL
To create [`ServersMetricCondition`](../newrelic-alerts-configurator#server-metric-condition) use `serverMetricCondition` dsl.

```kotlin
val config: ServersMetricCondition = serverMetricCondition {
    conditionName = "Condition name"
    enabled = true
    servers = listOf("some-host", "some-host-2")
    metric = ServersMetricCondition.Metric.CPU_PERCENTAGE
    runBookUrl = "runBookUrl"
    
    userDefined { /* configuration described in User defined configuration DSL section */ }
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# New Relic browser metric condition DSL
To create [`BrowserCondition`](../newrelic-alerts-configurator#new-relic-browser-metric-condition) use `browserCondition` dsl.

```kotlin
val config: BrowserCondition = browserCondition {
    conditionName = "Condition name"
    enabled = true
    applications = listOf("application 1", "application 2")
    metric = BrowserCondition.Metric.PAGE_VIEWS_WITH_JS_ERRORS
    runBookUrl = "runBookUrl"
    
    userDefined { /* configuration described in User defined configuration DSL section */ }
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# APM external service condition DSL
To create [`ApmExternalServiceCondition`](../newrelic-alerts-configurator#apm-external-service-condition) use `apmExternalServiceCondition` dsl.

```kotlin
val config: ApmExternalServiceCondition = apmExternalServiceCondition {
    conditionName = "Condition name"
    enabled = true
    applications = listOf("application 1", "application 2")
    externalServiceUrl = "externalServiceUrl"
    metric = ApmExternalServiceCondition.Metric.RESPONSE_TIME_AVERAGE
    runBookUrl = "runBookUrl"
    
    terms {
        term { /* configuration described in Term configuration DSL section */ }
        term { /* configuration described in Term configuration DSL section */ }
    }
}
```

# Alerts NRQL condition DSL
To create [`NrqlCondition`](../newrelic-alerts-configurator#alerts-nrql-condition) use `nrqlCondition` dsl.

```kotlin
val config: NrqlCondition = nrqlCondition {
    conditionName = "Condition name"
    enabled = true
    valueFunction = NrqlCondition.ValueFunction.SINGLE_VALUE
    query = "SELECT count(*) FROM `myApp:HealthStatus` WHERE healthy IS false"
    sinceValue = NrqlCondition.SinceValue.SINCE_5
    runBookUrl = "runBookUrl"
    
    terms {
        term { /* configuration described in NRQL term configuration DSL section */ }
        term { /* configuration described in NRQL term configuration DSL section */ }
    }
}
```

# Synthetics Monitor Failure condition DSL
To create [`SyntheticsCondition`](../newrelic-alerts-configurator#synthetics-monitor-failure-condition) use `syntheticsCondition` dsl.

```kotlin
val config: SyntheticsCondition = syntheticsCondition {
    conditionName = "MyMonitor failed"
    enabled = true
    monitorName = "MyMonitor"
    runBookUrl = "runBookUrl"
}
```

# User defined configuration DSL
To create [`UserDefinedConfiguration`](../newrelic-alerts-configurator#user-defined-configuration) use `userDefinedConfiguration` dsl.

```kotlin
val config: UserDefinedConfiguration = userDefinedConfiguration {
    metric = "MY_CUSTOM_METRIC"
    valueFunction = UserDefinedConfiguration.ValueFunction.MAX
}
```

# Term configurations DSL
To create [`TermsConfiguration`](../newrelic-alerts-configurator#term) use `termsConfiguration` dsl.

```kotlin
val config: TermsConfiguration = termsConfiguration {
    durationTerm = DurationTerm.DURATION_5
    operatorTerm = OperatorTerm.BELOW
    thresholdTerm = 0.8f
    priorityTerm = PriorityTerm.CRITICAL
    timeFunctionTerm = TimeFunctionTerm.ALL
}
```

# NRQL term configuration DSL
To create [`NrqlTermsConfiguration`](../newrelic-alerts-configurator#nrql-term) use `nrqlTermsConfiguration` dsl.

```kotlin
val config: NrqlTerms = nrqlTermsConfiguration {
    durationTerm = NrqlDurationTerm.DURATION_5
    operatorTerm = OperatorTerm.BELOW
    thresholdTerm = 0.8f
    priorityTerm = PriorityTerm.CRITICAL
    timeFunctionTerm = TimeFunctionTerm.ALL
}
```

# Email channel DSL
To create [`EmailChannel`](../newrelic-alerts-configurator#email-channel) use `emailChannel` dsl.

```kotlin
val config: EmailChannel = emailChannel {
    channelName = "Channel name"
    emailAddress = "your-email-address@ocado.com"
    includeJsonAttachment = true
}
```

# Slack channel DSL
To create [`SlackChannel`](../newrelic-alerts-configurator#slack-channel) use `slackChannel` dsl.

```kotlin
val config: SlackChannel = slackChannel {
    channelName = "Channel name"
    slackUrl = "https://hooks.slack.com/services/..."
    teamName = "My team slack name"
}
```

# Webhook channel DSL
To create [`WebhookChannel`](../newrelic-alerts-configurator#webhook-channel) use `webhookChannel` dsl.

```kotlin
val config: WebhookChannel = webhookChannel {
    channelName = "Channel name"
    baseUrl = "https://example.com/webhook/abcd123"
    authUsername = "john.doea"
    authPassword = "starwars1"
}
```

# PagerDuty channel DSL
To create [`PagerDutyChannel`](../newrelic-alerts-configurator#pagerduty-channel) use `pagerDutyChannel` dsl.

```kotlin
val config: PagerDutyChannel = pagerDutyChannel {
    channelName = "Channel name"
    serviceKey = "1234-5678-ABCD-XYZ"
}
```

# NewRelic user channel DSL
To create [`UserChannel`](../newrelic-alerts-configurator#newrelic-user-channel) use `userChannel` dsl.

```kotlin
val config: UserChannel = userChannel {
    emailAddress = "your-email-address@ocado.com"
}
```