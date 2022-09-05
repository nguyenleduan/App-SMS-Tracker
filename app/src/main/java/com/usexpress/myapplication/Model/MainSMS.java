package com.usexpress.myapplication.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainSMS {
    public ArrayList<ItemModel> arrSMS;
    public boolean isSendSMS;
    public String Content;
    public String date;
    public String phone;
    public String timeStartTracker;
    public String timeEndTracker;

    public MainSMS(ArrayList<ItemModel> arrSMS, boolean isSendSMS, String content, String date, String phone, String timeStartTracker, String timeEndTracker) {
        this.arrSMS = arrSMS;
        this.isSendSMS = isSendSMS;
        Content = content;
        this.date = date;
        this.phone = phone;
        this.timeStartTracker = timeStartTracker;
        this.timeEndTracker = timeEndTracker;
    }
    public Date getDateStart(){
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            return formatter1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
