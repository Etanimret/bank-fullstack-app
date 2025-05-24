package com.example.app.model.constant;

public enum ChannelCode {
    OTC("Over the Counter"),
    ATS("Automated Trading System");

    private final String description;

    ChannelCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
