package com.gymtrainer.trainerapp.Models;

/**
 * Created by haroonpc on 3/21/2019.
 */

public class WorkingHrs
{
    public String hourName;

    boolean isSelected;

    public WorkingHrs(String hourName, boolean isSelected) {
        this.hourName = hourName;
        this.isSelected = isSelected;
    }

    public String getHourName() {
        return hourName;
    }

    public void setHourName(String hourName) {
        this.hourName = hourName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
