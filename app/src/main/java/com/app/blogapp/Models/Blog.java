package com.app.blogapp.Models;

import java.io.Serializable;

public class Blog implements Serializable {
    int ID;
    String blogTitle;
    String blogDescription;
    String imagePath;
    String date;
    boolean isMarkedForDeletion;
    public Blog(int ID, String blogTitle, String blogDescription, String imagePath, String date) {
        this.ID = ID;
        this.blogTitle = blogTitle;
        this.blogDescription = blogDescription;
        this.imagePath = imagePath;
        this.date = date;
        isMarkedForDeletion = false;
    }

    public String getDate() {
        return date;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.isMarkedForDeletion = markedForDeletion;
    }
    public boolean getMarkedForDeletion() {
        return isMarkedForDeletion;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
