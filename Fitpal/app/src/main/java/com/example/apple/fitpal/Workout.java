package com.example.apple.fitpal;

class Workout {

    private String title;
    private String wod;

    public Workout(String title, String wod) {
        this.title = title;
        this.wod = wod;
    }

    public Workout() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWod() {
        return wod;
    }

    public void setWod(String wod) {
        this.wod = wod;
    }
}
