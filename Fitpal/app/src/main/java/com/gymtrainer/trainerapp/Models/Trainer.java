package com.gymtrainer.trainerapp.Models;

import java.io.Serializable;



public class Trainer implements Serializable
{
    public Trainer(){};

    public String name,email,phonenumber,address,city,gender,trailtraining,experience,about,rate,imageUrl,trainerid,lat,lng;

    public Trainer(String name, String email, String phonenumber, String address, String city, String gender, String trailtraining,
                   String experience, String about,String rate,String imageUrl,String trainerid,String lat,String lng) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.city = city;
        this.gender = gender;
        this.trailtraining = trailtraining;
        this.experience = experience;
        this.about = about;
        this.rate = rate;
        this.trainerid = trainerid;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lng = lng;

    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrainerid() {
        return trainerid;
    }

    public void setTrainerid(String trainerid) {
        this.trainerid = trainerid;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTrailtraining() {
        return trailtraining;
    }

    public void setTrailtraining(String trailtraining) {
        this.trailtraining = trailtraining;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


}
