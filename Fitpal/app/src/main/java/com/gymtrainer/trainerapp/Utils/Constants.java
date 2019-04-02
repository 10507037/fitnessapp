package com.gymtrainer.trainerapp.Utils;

import com.gymtrainer.trainerapp.Models.WorkingHrs;

import java.util.ArrayList;

/**
 * Created by haroonpc on 3/21/2019.
 */

public class Constants
{
    public static String[] hourItems = {"8 AM","9 AM","10 AM","11 AM","12 PM","01 PM","02 PM","03 PM","04 PM","05 PM"};

    public static ArrayList<WorkingHrs> getHoursList(){
        ArrayList<WorkingHrs> list = new ArrayList<>();
        for (String item:hourItems) {
            list.add(new WorkingHrs(item,false));
        }

        return list;
    }

}
