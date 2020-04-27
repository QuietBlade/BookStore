package com.bookstroe.demo01.beans;

public class Book {
    private String id;
    private String name;
    private Double price;
    private String classif;
    private int num;
    private String imgurl;
    private String desc;

    public Double getPrice() {
        return price;
    }

    public int getNum() {
        return num;
    }

    public String getClassif() {
        return classif;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getName() {
        return name;
    }

    public void setClassif(String classif) {
        this.classif = classif;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
}
