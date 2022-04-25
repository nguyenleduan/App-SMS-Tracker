package com.usexpress.myapplication.Model;

public class ApiRequet {
    public int Code;
    public String Message;
    public String Data;

    public ApiRequet(int code, String message, String data) {
        Code = code;
        Message = message;
        Data = data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
