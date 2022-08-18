package com.usexpress.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.TokenModel;

import java.util.ArrayList;
import java.util.List;

public class DataSetting {
    public static DataModel dataProfile = new DataModel();
    public static String Token = "1";
    public static String DoMain = "";
    public static String UrlApi = "";
    public static int TimeDelay = 20;
    public static Context context;
    public static boolean isUrlToken = false;
    public static ArrayList<SMSModel> arrayListSMS = new ArrayList<>();
    public static ArrayList<ItemModel> arrayListData = new ArrayList<>();
}
