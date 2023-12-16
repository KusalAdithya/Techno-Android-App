package com.waka.techno.model;

import java.util.Date;

public class Notification {
    private int id;
    private String title;
    private String description;
    private String dateTime;
    private int img;

    public Notification(String title, String description, String dateTime,int img) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.img = img;
    }

    public Notification(String title, String description, String dateTime) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
