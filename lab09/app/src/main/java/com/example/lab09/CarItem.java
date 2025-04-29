package com.example.lab09;

public class CarItem {
    private String imageUrl;
    private String description;
    private String webUrl;

    public CarItem(String imageUrl, String description, String webUrl) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.webUrl = webUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getWebUrl() {
        return webUrl;
    }
}

