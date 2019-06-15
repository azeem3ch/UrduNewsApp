package com.example.urdunews;

import java.io.Serializable;

public class News implements Serializable {
    String image;
    String title;
    String date;
    String description;
    String id;



    String catagory;

    public News(String image, String title, String date, String description, String id,String catagory) {

        this.image = image;
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
        this.catagory=catagory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagory() { return catagory; }

    public void setCatagory(String catagory) { this.catagory = catagory;    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
