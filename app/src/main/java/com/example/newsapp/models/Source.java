package com.example.newsapp.models;

import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public Source(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
