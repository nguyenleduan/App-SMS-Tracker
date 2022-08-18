package com.usexpress.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.usexpress.myapplication.Activity.HomeActivity;
import com.usexpress.myapplication.Activity.SettingActivity;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.bodyModel;
import com.usexpress.myapplication.Service.CallApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class Serviced extends Service {
    public static int counter = 0;
    private int countTime = DataSetting.TimeDelay;
    private int countSaveTime = 1;
    HomeActivity homeActivity = new HomeActivity();
    private Timer timer ;
    private TimerTask timerTask;
    CompletableFuture<ArrayList<SMSModel>> completableFuture = new CompletableFuture<>();
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        getCacheToken();
    }
    public void startTimerCallApi() {
        Log.i("TOKEN", "-------- " + DataSetting.Token);
        ContentResolver cr = getContentResolver();
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if (counter == DataSetting.TimeDelay) {
                   if(DataSetting.Token.equals("1")){
                       getCacheToken();
                   }
                    getSMS(cr );
                    timer.cancel();
                    counter = 0;
                    Toaster.toast("App SMS running...");
                    startTimerCallApi();
                }
//                if(counter == 0){
//                    saveCache();
//                }
                Log.i("Count", "--------" + counter);
                counter++;
            }
        };
        timer.schedule(timerTask, 10000, 10000); //
    }

    public void getSMS(ContentResolver cr ) {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
//        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
        DataSetting.arrayListSMS.clear();
        if (!cursor.moveToFirst()) {
        }
        try {
            final int idIndex = cursor.getColumnIndex("_id");
            final int nameIndex = cursor.getColumnIndex("address");
            final int descriptionIndex = cursor.getColumnIndex("body");
            final int dateSMS = cursor.getColumnIndex("date");
            //TODO ??
            if (!cursor.moveToFirst()) {
                return ;
            }
            DataSetting.arrayListSMS.clear();
            do {
                final long id = cursor.getLong(idIndex);
                final String phone = cursor.getString(nameIndex);
                final String body = cursor.getString(descriptionIndex);
                Time time = new Time();
                time.setToNow();
                final String date = DateFormat.format("yyyy/MM/dd HH:mm:ss", new Date(cursor.getLong(dateSMS))).toString();
                /// TODO check phone add list
                if (checkDataCache()) {
                    String[] list = DataSetting.dataProfile.AllowPhone.split(",");
                    for (int i = 0; i < list.length; i++) {
                        if (phone.contains(list[i]) && cursor.getLong(dateSMS) > DataSetting.dataProfile.DateStart) {
                            DataSetting.arrayListSMS.add(new SMSModel(id, phone, body, date));
                            Log.d("SMS: ----------", " Phone: "+phone+ "    Body"+body);
                        }
                    }
                }
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
        checkSMS();
    }

    public boolean checkDataCache(){
        if(DataSetting.dataProfile == null
                || DataSetting.dataProfile.AllowPhone == null   ){
            return false;
        }
        return true;
    }
    void getCacheToken(){
        SharedPreferences sharedPref =   this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        DataSetting.Token = sharedPref.getString("KeyToken", "1");
        Log.i("GET TOKEN", "----TOKEN----");
    }
    public void checkSMS() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateNow = formatter.format(date);
        if (DataSetting.arrayListSMS == null || DataSetting.arrayListSMS.size() == 0) {
            Toaster.toast("Không đọc được SMS nào !!");
        } else {
            for(int i = 0; i<DataSetting.arrayListSMS.size();i++){
                if(checkNew(DataSetting.arrayListSMS.get(i).getID())){
                    callApi(DataSetting.arrayListSMS.get(i).getID(),
                            DataSetting.arrayListSMS.get(i).getPhone(),
                            DataSetting.arrayListSMS.get(i).getBody(),
                            DataSetting.arrayListSMS.get(i).getDate(),dateNow );
                }
            }
            checkFail();
        }
    }

    public boolean checkNew(long id ) {
        if(DataSetting.arrayListData == null || DataSetting.arrayListData.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < DataSetting.arrayListData.size(); i++) {
                if (DataSetting.arrayListData.get(i).getID() == id ) {
                    return false;
                }
            }
            return true;
        }
    }
    public void checkFail(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateNow = formatter.format(date);
        Log.d("Check fail : -----------" , " zise arrayListData: "+DataSetting.arrayListData.size()+" - arrayListSMS: "+DataSetting.arrayListSMS.size());
        for (int i = 0; i < DataSetting.arrayListData.size(); i++) {
            if (!DataSetting.arrayListData.get(i).isSucceeded()) {
                callApi(DataSetting.arrayListSMS.get(i).getID(),
                        DataSetting.arrayListSMS.get(i).getPhone(),
                        DataSetting.arrayListSMS.get(i).getBody(),
                        DataSetting.arrayListSMS.get(i).getDate(),dateNow);
             }
        }
    }
    public void checkSaveCache(ItemModel model ){
        if(DataSetting.arrayListData.size()==0){
            DataSetting.arrayListData.add(model);
        }else {
            if(checkNew(model.getID() )){
                DataSetting.arrayListData.add(model);
            }else{
                for(int i=0; i<DataSetting.arrayListData.size();i++){
                    if(DataSetting.arrayListData.get(i).getID() == model.getID()){
                        DataSetting.arrayListData.get(i).setSucceeded(model.isSucceeded());
                    }
                }
            }
            saveCache();
        }
    }
    static final String MY_PREFS_NAME = "MyPrefsFile";
    void saveCache(){
        try {
            SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(DataSetting.arrayListData);
            prefsEditor.putString("arrDataKey", json);
            prefsEditor.apply();
            Log.d(" Cache" , "------------Save OK---------- ");
        }catch (Exception e){
            Log.d("Cache ERROR : " , "---------------------- "+e);
        }
    }
    public Boolean checkDouble(long index){
        for(int i =0 ;i< DataSetting.arrayListData.size();i++){
            if(DataSetting.arrayListData.get(i).getID() == index && DataSetting.arrayListData.get(i).isSucceeded()){
                return  false;
            }
        }
        return  true;
    }
    public Boolean callApi(long id, String phone, String body, String dateSMS, String dateNow ) {
        bodyModel model = new bodyModel(phone, body, dateSMS);
        if(checkDouble(id)){
            Log.d("Call Api:  " , "-------------------"+phone+"  body: "+ body);
            CallApi.apiService.PostSMS(DataSetting.UrlApi, model).enqueue(new Callback<ApiRequet>() {
                @Override
                public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
                    if (response.code() == 200) {
                        checkSaveCache(new ItemModel(id, phone, body, dateSMS, dateNow, true));
                        Toaster.toast("Đã gửi nội dung: ID:"+id+" [Phone: " + phone + "]");

                    } else {
                        checkSaveCache(new ItemModel(id, phone, body, dateSMS, "Gửi Api fail " + response.code(), false));
                        Toaster.toast("Gửi Thông tin thất bại:  ID:"+id+"  " + response.code() + " --- [Phone: " + phone + "]");
                    } return ;
                }
                @Override
                public void onFailure(Call<ApiRequet> call, Throwable t) {
                    checkSaveCache(new ItemModel(id, phone, body, dateSMS, "Error Api", false));
                    Toaster.toast("Gửi Thông tin thất bại: ID: "+id+ "  [Phone: " + phone + "]");

                }
            });
        }
        return true ;

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimerCallApi();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}