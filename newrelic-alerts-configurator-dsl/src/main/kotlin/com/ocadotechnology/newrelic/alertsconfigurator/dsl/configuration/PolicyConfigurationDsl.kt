package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.PolicyConfiguration
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.channel.Channel
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.SyntheticsCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.channel.Channels
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition.Conditions

@NewRelicConfigurationMarker
class PolicyConfigurationDsl {
    var policyName: String? = null
    var incidentPreference: PolicyConfiguration.IncidentPreference? = null
    internal val channels: MutableList<Channel> = mutableListOf()
    internal val conditions: MutableList<Condition> = mutableListOf()
    internal val externalServiceConditions: MutableList<ExternalServiceCondition> = mutableListOf()
    internal val nrqlConditions: MutableList<NrqlCondition> = mutableListOf()
    internal val syntheticsConditions: MutableList<SyntheticsCondition> = mutableListOf()

    fun channels(block: Channels.() -> Unit) = channels.addAll(Channels().apply(block).channels)

    fun conditions(block: Conditions.() -> Unit) {
        val conds = Conditions().apply(block)
        conditions.addAll(conds.conditions)
        externalServiceConditions.addAll(conds.externalServiceConditions)
        nrqlConditions.addAll(conds.nrqlConditions)
        syntheticsConditions.addAll(conds.syntheticsConditions)
    }
}

// TODO maybe it should be object with invoke operator instead of method?
fun policy(block: PolicyConfigurationDsl.() -> Unit): PolicyConfiguration {
    val dsl = PolicyConfigurationDsl()
    dsl.block()

    return PolicyConfiguration.builder()
            .policyName(dsl.policyName)
            .incidentPreference(dsl.incidentPreference)
            .channels(dsl.channels)
            .conditions(dsl.conditions)
            .externalServiceConditions(dsl.externalServiceConditions)
            .nrqlConditions(dsl.nrqlConditions)
            .syntheticsConditions(dsl.syntheticsConditions)
            .build()
}

@NewRelicConfigurationMarker
class PolicyConfigurations {
    internal val policies: MutableList<PolicyConfiguration> = mutableListOf()

    fun policy(block: PolicyConfigurationDsl.() -> Unit) = policies.add(com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.policy(block))
    operator fun PolicyConfiguration.unaryPlus() = policies.add(this)
}
