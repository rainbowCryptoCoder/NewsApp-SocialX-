package com.example.newsapp.data.remote;

public class NewsApiClient {

    private static NewsApiClient instance;

    public NewsApiClient() {
    }

    public static NewsApiClient getInstance() {
        if (instance == null) {
            instance = new NewsApiClient();
        }
        return instance;
    }
}
