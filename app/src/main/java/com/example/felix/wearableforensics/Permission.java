package com.example.felix.wearableforensics;

/**
 * Created by felix on 10/09/2016.
 */
public class Permission {

    private String name;
    private String description;

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}