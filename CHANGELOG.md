# 3.0.0
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
