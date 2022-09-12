package com.usexpress.myapplication.Model;

public class ItemModel {
    private String phone;
    private String Message;
    private long ID;
    private String Date_SMSArrived;
    private String Date_CallSuccessful ;
    private boolean Succeeded;
    private int Answer; // 0 chua gui // 2 da qua

    public ItemModel(long ID, String phone, String message, String date_SMSArrived, String date_CallSuccessful, boolean succeeded, int answer) {
        this.ID = ID;
        this.phone = phone;
        Message = message;
        Date_SMSArrived = date_SMSArrived;
        Date_CallSuccessful = date_CallSuccessful;
        Succeeded = succeeded;
        Answer = answer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getDate_SMSArrived() {
        return Date_SMSArrived;
    }

    public void setDate_SMSArrived(String date_SMSArrived) {
        Date_SMSArrived = date_SMSArrived;
    }

    public String getDate_CallSuccessful() {
        return Date_CallSuccessful;
    }

    public void setDate_CallSuccessful(String date_CallSuccessful) {
        Date_CallSuccessful = date_CallSuccessful;
    }

    public boolean isSucceeded() {
        return Succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        Succeeded = succeeded;
    }

    public int getAnswer() {
        return Answer;
    }

    public void setAnswer(int answer) {
        Answer = answer;
    }
}
