package com.ocadotechnology.newrelic.alertsconfigurator.dsl.configuration.condition

import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.Condition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.ExternalServiceCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.NrqlCondition
import com.ocadotechnology.newrelic.alertsconfigurator.configuration.condition.SyntheticsCondition
import com.ocadotechnology.newrelic.alertsconfigurator.dsl.NewRelicConfigurationMarker

@NewRelicConfigurationMarker
class Conditions {
    internal val conditions: MutableList<Condition> = mutableListOf()
    internal val externalServiceConditions: MutableList<ExternalServiceCondition> = mutableListOf()
    internal val nrqlConditions: MutableList<NrqlCondition> = mutableListOf()
    internal val syntheticsConditions: MutableList<SyntheticsCondition> = mutableListOf()

    fun apmApp(block: ApmAppConditionDsl.() -> Unit) = conditions.add(apmAppCondition(block))
    fun apmExternalService(block: ApmExternalServiceConditionDsl.() -> Unit) = externalServiceConditions.add(apmExternalServiceCondition(block))
    fun apmJvm(block: ApmJvmConditionDsl.() -> Unit) = conditions.add(apmJvmCondition(block))
    fun apmKeyTransation(block: ApmKeyTransactionConditionDsl.() -> Unit) = conditions.add(apmKeyTransactionCondition(block))
    fun browser(block: BrowserConditionDsl.() -> Unit) = conditions.add(browserCondition(block))
    fun nrql(block: NrqlConditionDsl.() -> Unit) = nrqlConditions.add(nrqlCondition(block))
    fun serversMetric(block: ServersMetricConditionDsl.() -> Unit) = conditions.add(serversMetricCondition(block))
    fun synthetics(block: SyntheticsConditionDsl.() -> Unit) = syntheticsConditions.add(syntheticsCondition(block))
    operator fun Condition.unaryPlus() = conditions.add(this)
    operator fun ExternalServiceCondition.unaryPlus() = externalServiceConditions.add(this)
    operator fun NrqlCondition.unaryPlus() = nrqlConditions.add(this)
    operator fun SyntheticsCondition.unaryPlus() = syntheticsConditions.add(this)
}