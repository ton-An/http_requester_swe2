package com.swe2.http_requester;

public class TableContent {
    private String name;
    private String value;

    public TableContent(String name, String value) {
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