package com.gymtrainer.trainerapp.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haroonpc on 3/25/2019.
 */

public class Hire
{
    public String userId,trainerId,categoryName,name,imageUrl,rate,date;
    public ArrayList<String> hourList;
    public Hire(){}

    public Hire(String userId, String trainerId, String categoryName,String name,String imageUrl,
                String rate, ArrayList<String> hourList,String date) {
        this.userId = userId;
        this.trainerId = trainerId;
        this.categoryName = categoryName;
        this.name = name;
        this.imageUrl = imageUrl;
        this.rate = rate;
        this.hourList = hourList;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<String> getHourList() {
        return hourList;
    }

    public void setHourList(ArrayList<String> hourList) {
        this.hourList = hourList;
    }
}
