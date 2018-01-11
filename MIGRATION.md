# Migrating from version 2 to 3

In version 3 we changed the way condition synchronization works. In version 3 all conditions are managed in the same way.

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
   
In version 2 there was no way to preserve already existing conditions. Existing conditions were removed in both cases: when you 
provided empty conditions list and when you didn't provide anything at all