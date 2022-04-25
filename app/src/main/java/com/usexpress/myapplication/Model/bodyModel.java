package com.usexpress.myapplication.Model;

public class bodyModel {

    public String Number;
    public String Message;
    public String Time;

    public bodyModel(String number, String message, String time) {
        Number = number;
        Message = message;
        Time = time;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
