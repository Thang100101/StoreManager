package com.example.yourshopapplication.Adapter;

import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ConvertDatetime {
    public static Date getDate(String date){
        Date dateTime = null;
        try {
            dateTime = new SimpleDateFormat("dd/MM/yyyy-HH/mm").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
    public static String getDateString(Date dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH/mm");
        return simpleDateFormat.format(dateTime);
    }
}
