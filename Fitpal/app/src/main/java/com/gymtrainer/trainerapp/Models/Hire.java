package com.gymtrainer.trainerapp.Models;

import java.util.List;

/**
 * Created by haroonpc on 3/25/2019.
 */

public class Hire
{
    public String userId,trainerId,categoryName;
    public List<String> hourList;

    public Hire(String userId, String trainerId, String categoryName, List<String> hourList) {
        this.userId = userId;
        this.trainerId = trainerId;
        this.categoryName = categoryName;
        this.hourList = hourList;
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

    public List<String> getHourList() {
        return hourList;
    }

    public void setHourList(List<String> hourList) {
        this.hourList = hourList;
    }
}
