package com.usexpress.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.TokenModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xdroid.toaster.Toaster;

public class DataSetting {
    public static DataModel dataProfile = new DataModel();
    public static String Token = "12312";
    public static  String KeySMS = "KeySMS";
    public static  String KeyTime = "KeyTime";
    public static  String KeyContent = "KeyContent";
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

    public  void setValueArrMainSMS(int index,MainSMS model,Context context){
            if(model!=null){
                DataSetting.arraySMSMain.set(index,model);
                save(context);
            }

    }
    public void save(Context context){
        try {
            SharedPreferences mPrefs = context.getSharedPreferences("MyPrefsFile", 0);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(DataSetting.arraySMSMain);
            prefsEditor.putString(DataSetting.KeySMS, json);
            prefsEditor.apply();
            Log.d("Save cache SMS", "----------Save cache success");
        }catch (Exception e){
            Log.d("Save cache SMS", "----------ERROR");
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
    public  void addContent(String content){
        if(content != null && !content.isEmpty()){
            arrayContentSendSMS.add(""+content);
        }
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
    public  int AnswerAction(String phoneNumber,String message){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    null,
                    null);
            return 1;
        }catch (Exception e){
            Log.e("Error send SMS ",""+e);
            return -1;
        }
    }


    public boolean isAnswerTime(String start,String end){
        try{
            if(start!= null && end !=null){
                if(start.equals(end)){
                    return true;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("HH");
                Date date = new Date();
                String dateNow = formatter.format(date);
                Log.d("check time: ","      "+dateNow);
                if (Integer.parseInt(dateNow)>=Integer.parseInt(start) && Integer.parseInt(dateNow) <=Integer.parseInt(end)) {
                    Log.d("check time: ","   OK   "+dateNow);
                    return true;
                }
            }
        }catch (Exception e){
            Log.d("check time: ","    ERROR  ");
        }
        return false;
    }
}
