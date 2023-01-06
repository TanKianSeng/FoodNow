package com.example.androideatit.Common;

import com.example.androideatit.Model.User;

public class Common {
    public static User User_current;

    public static String convertCodeToStatus(String status){
        if (status.equals("0")){
            return "Placed";
        }else if (status.equals("1")){
            return "On my way";
        }else{
            return "Shipped";
        }
    }
}
