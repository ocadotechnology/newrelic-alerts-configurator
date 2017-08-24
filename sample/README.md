# Sample

This is the sample project that uses NewRelic Alerts Configurator library to configure NewRelic alerts for few applications.

## Goals

We have **3** applications:
- `Application1` deployed on `app-1-host`
- `Application2` deployed on `app-2-host`
- `Application3` deployed on `app-3-host`

For **each** application we need to:
- Raise warning alert if **apdex** falls below `0.85`
- Raise critical alert if **apdex** falls below `0.7`
- Send alert notification to **slack** to `newrelic-alerts` channel

For **Application1** we need to additionally:
- Raise warning alert if **disk space** on application host raises above `65%`
- Raise critical alert if **disk space** on application host raises above `80%`
- Send alert notifications to **email** `my-team@my-company.com`

For **Application3** we need to additionally:
- Raise warning alert if **cpu usage** on application host raised above `70%`
- Raise critical alert if **cpu usage** on application host raised above `90%`
- Send alert notifications to **email** `my-team@my-company.com`

Moreover, described configuration should be deployed on **two** different NewRelic account:
- one account monitoring staging environment
- one account monitoring production environment