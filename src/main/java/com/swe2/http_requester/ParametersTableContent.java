package com.swe2.http_requester;

/**
 * Represents a row in the parameters table
 */
public class ParametersTableContent {
    private String name;
    private String value;

    public ParametersTableContent(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}