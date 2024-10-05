package com.mountreachsolution.foodizapp;

public class POJOCategoryWiseDish {
    String id,categoryname,restname,dishcategory,dishimg,dishname,dishprice,dishoffer,dishrating,dishdescription;

    public POJOCategoryWiseDish(String id, String categoryname, String restname, String dishcategory, String dishimg, String dishname, String dishprice, String dishoffer, String dishrating, String dishdescription) {
        this.id = id;
        this.categoryname = categoryname;
        this.restname = restname;
        this.dishcategory = dishcategory;
        this.dishimg = dishimg;
        this.dishname = dishname;
        this.dishprice = dishprice;
        this.dishoffer = dishoffer;
        this.dishrating = dishrating;
        this.dishdescription = dishdescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public String getDishcategory() {
        return dishcategory;
    }

    public void setDishcategory(String dishcategory) {
        this.dishcategory = dishcategory;
    }

    public String getDishimg() {
        return dishimg;
    }

    public void setDishimg(String dishimg) {
        this.dishimg = dishimg;
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getDishprice() {
        return dishprice;
    }

    public void setDishprice(String dishprice) {
        this.dishprice = dishprice;
    }

    public String getDishoffer() {
        return dishoffer;
    }

    public void setDishoffer(String dishoffer) {
        this.dishoffer = dishoffer;
    }

    public String getDishrating() {
        return dishrating;
    }

    public void setDishrating(String dishrating) {
        this.dishrating = dishrating;
    }

    public String getDishdescription() {
        return dishdescription;
    }

    public void setDishdescription(String dishdescription) {
        this.dishdescription = dishdescription;
    }
}
