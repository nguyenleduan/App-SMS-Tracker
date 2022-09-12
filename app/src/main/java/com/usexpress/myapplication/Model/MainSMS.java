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
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            return formatter1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ItemModel> getArrSMS() {
        return arrSMS;
    }

    public void setArrSMS(ArrayList<ItemModel> arrSMS) {
        this.arrSMS = arrSMS;
    }

    public boolean isSendSMS() {
        return isSendSMS;
    }

    public void setSendSMS(boolean sendSMS) {
        isSendSMS = sendSMS;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimeStartTracker() {
        return timeStartTracker;
    }

    public void setTimeStartTracker(String timeStartTracker) {
        this.timeStartTracker = timeStartTracker;
    }

    public String getTimeEndTracker() {
        return timeEndTracker;
    }

    public void setTimeEndTracker(String timeEndTracker) {
        this.timeEndTracker = timeEndTracker;
    }
}
