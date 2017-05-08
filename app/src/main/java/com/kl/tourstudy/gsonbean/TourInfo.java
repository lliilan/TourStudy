package com.kl.tourstudy.gsonbean;

/**
 * Created by KL on 2017/4/17 0017.
 */

public class TourInfo {

    private int id;
    private String name;
    private String image;
    private int price;
    private String endSignDate;
    private String day;
    private String picInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEndSignDate() {
        return endSignDate;
    }

    public void setEndSignDate(String endSignDate) {
        this.endSignDate = endSignDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(String picInfo) {
        this.picInfo = picInfo;
    }

}
