package com.ekarantechnologies.wandi.models;

public class ScreenItem {
    private String title,Description;
    private String Image;

    public ScreenItem(String title, String description, String image) {
        this.title = title;
        Description = description;
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }
}
