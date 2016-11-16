package com.ocado.pandateam.newrelic.sync.configuration.condition;

public interface Condition {
    String getConditionName();
    boolean isEnabled();
    String getMetric();
    String getRunBookUrl();
    String getDurationTerm();
    String getOperatorTerm();
    String getPriorityTerm();
    int getThresholdTerm();
    String getTimeFunctionTerm();
}
