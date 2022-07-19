# 4.2.0 (19-07-2022)
- Added possibility to define custom duration

# 4.1.0 (06-07-2022)
- Removed deprecated `sinceValue` from Nrql Condition

# 4.0.1 (03-06-2022)
- Allow usage of aggregationTimer when EVENT_TIMER streaming method is used

# 4.0.0 (19-05-2022)
- Adapt to Signal having aggregationMethod

# 3.6.1 (13-10-2021)
- Added support for signal configuration without expiration

# 3.6.0 (09-09-2021)
- Added support for `signalFillValue` in NRQL Signaling
- Updated conditions REST API url

# 3.5.3 (25-08-2021)
- Added SyntheticsMonitorsApi

# 3.5.2 (08-03-2021)
- Added NRQL signal configuration to Kotlin DSL

# 3.5.1 (14-01-2021)
- Fixed setting explicit null value for field 'fill_value' in Signal configuration

# 3.5.0 (02-12-2020)
- Added Signal configuration

# 3.4.1 (26-02-2020)
- Added new GcMetric `GC/MarkSweepCompact` and `GC/Copy` 

# 3.4.0 (06-06-2019)
- Added newrelic-alerts-configurator-dsl module with kotlin dsl

# 3.3.1 (03-10-2018)
- Added more "Since" values to NRQL Condition

# 3.3.0 (07-08-2018)
- Added support for Synthetics Monitor Failure conditions
- Removed unused AlertsNrqlConditionsApi interface

# 3.2.0 (09.04.2018)
- Added new GcMetric `GC/ParNew` and `GC/ConcurrentMarkSweep` 

# 3.1.0 (23.02.2018)
- Added support for New Relic Browser conditions 

# 3.0.0 (17.01.2018)
- Added support for NRQL conditions
- Changed conditions and channels management logic. Changed default conditions state. Those are breaking changes - see details in 
[Migration guide](MIGRATION.md)

# 2.0.3 (06.12.2017)
- Added support for JVM metrics conditions

# 2.0.2 (20.09.2017)
- Add violation_close_timer
- Make incident preference required in policy configuration

# 2.0.0 (15.02.2017)
- Changed groupdId and artifactId
- Changed packages to `com.ocadotechnology.newrelic`
- Changed `AlertsChannelConfiguration` field `payload` to type `Object`
- JSON deserialization configured to convert empty string into null POJO reference 

# 1.4.0 (06.02.2017)
- Added support for user notification channel 
- Simplified channel synchronization logic

# 1.3.0 (24.01.2017)
- Added UserDefined condition type support for `ApmAppCondition` and `ServersMetricCondition`

# 1.2.0 (19.01.2017)
- Added PagerDuty channel support

# 1.1.2
- Added Webhook channel support

# 1.1.1
- fixed Maven publication problems

# 1.1.0
- added support for server metric conditions
- fixed type of `links` property in `KeyTransaction` 
