package com.usexpress.myapplication.Model;

public class SMSModel {
    private long ID;
    private String phone;
    private String body;
    private String Date;

    public SMSModel(long ID, String phone, String body, String date) {
        this.ID = ID;
        this.phone = phone;
        this.body = body;
        Date = date;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
