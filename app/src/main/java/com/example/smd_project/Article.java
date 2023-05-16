package com.example.smd_project;

import java.io.Serializable;

public class Article implements Serializable {

    private String Image;
    private String title;

    public Article()
    {

    }

    public Article(String image, String title) {
        Image = image;
        this.title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
