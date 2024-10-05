package com.mountreachsolution.foodizapp;

public class POJOGetAllCategories {

    String id,categoryImage,categoryName;

    public POJOGetAllCategories(String id, String categoryImage, String categoryName) {
        this.id = id;
        this.categoryImage = categoryImage;
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }
}
