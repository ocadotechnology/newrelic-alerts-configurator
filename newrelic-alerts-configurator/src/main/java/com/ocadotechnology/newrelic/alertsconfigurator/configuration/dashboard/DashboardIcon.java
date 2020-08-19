package com.ocadotechnology.newrelic.alertsconfigurator.configuration.dashboard;

import lombok.Getter;

@Getter
public enum DashboardIcon {
    NONE("none"),
    ARCHIVE("archive"),
    BAR_CHART("bar-chart"),
    LINE_CHART("line-chart"),
    BULLSEYE("bullseye"),
    USER("user"),
    USD("usd"),
    MONEY("money"),
    THUMBS_UP("thumbs-up"),
    THUMBS_DOWN("thumbs-down"),
    CLOUD("cloud"),
    BELL("bell"),
    BULLHORN("bullhorn"),
    COMMENTS_O("comments-o"),
    ENVELOPE("envelope"),
    GLOBE("globe"),
    SHOPPING_CART("shopping-cart"),
    SITEMAP("sitemap"),
    CLOCK_O("clock-o"),
    CROSSHAIRS("crosshairs"),
    ROCKET("rocket"),
    USERS("users"),
    MOBILE("mobile"),
    TABLET("tablet"),
    ADJUST("adjust"),
    DASHBOARD("dashboard"),
    FLAG("flag"),
    FLASK("flask"),
    ROAD("road"),
    BOLT("bolt"),
    COG("cog"),
    LEAF("leaf"),
    MAGIC("magic"),
    PUZZLE_PIECE("puzzle-piece"),
    BUG("bug"),
    FIRE("fire"),
    LEGAL("legal"),
    TROPHY("trophy"),
    PIE_CHART("pie-chart"),
    SLIDERS("sliders"),
    PAPER_PLANE("paper-plane"),
    LIFE_RING("life-ring"),
    HEART("heart");

    private final String icon;

    DashboardIcon(String icon) {
        this.icon = icon;
    }
}
