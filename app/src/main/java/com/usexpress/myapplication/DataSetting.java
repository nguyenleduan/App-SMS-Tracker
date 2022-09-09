package com.usexpress.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.TokenModel;

import java.util.ArrayList;
import java.util.List;

public class DataSetting {
    public static DataModel dataProfile = new DataModel();
    public static String Token = "12312";
    public static  String KeySMS = "KeySMS";
    public static  String KeyTime = "KeyTime";
    public static String DoMain = "";
    public static String myPhoneNumber = "";
    public static String UrlApi = "";
    public static int TimeDelay = 30;
    public static Context context;
    public static boolean isUrlToken = false;
    public static ArrayList<SMSModel> arrayListSMS = new ArrayList<>();
    public static ArrayList<String> arrayListPhone = new ArrayList<>();
    public static ArrayList<String> arrayContentSendSMS = new ArrayList<>();
    public static ArrayList<MainSMS> arraySMSMain = new ArrayList<>();
    public static ArrayList<ItemModel> arrayListData = new ArrayList<>();
    public  void setValueArrMainSMS(int index,MainSMS model){
        if(model!=null){
            DataSetting.arraySMSMain.set(index,model);
        }
    }
    public int getIndexListContent(String content){
        for(int i =0 ;i<arrayContentSendSMS.size();i++){
            if(content.equals(arrayContentSendSMS.get(i))){
                return i;
            }
        }
        return -1;
    }
    public void setContent(int index,String content){
        MainSMS sms = new MainSMS(
                arraySMSMain.get(index).arrSMS,
                arraySMSMain.get(index).isSendSMS,content,
                arraySMSMain.get(index).date,
                arraySMSMain.get(index).phone,
                arraySMSMain.get(index).timeStartTracker,
                arraySMSMain.get(index).timeEndTracker);
        arraySMSMain.set(index,sms);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
