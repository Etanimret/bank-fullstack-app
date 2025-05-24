package com.example.app.model.constant;

public enum StatementCode {
    A0("Deposit"),
    A1("Withdrawal"),
    A2("Transfer"),
    A3("Received");

    private final String description;

    StatementCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
