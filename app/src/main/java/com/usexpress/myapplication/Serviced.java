package com.usexpress.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.usexpress.myapplication.Activity.HomeActivity;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.Fragment.ListTreackerFragment;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.bodyModel;
import com.usexpress.myapplication.Service.ApiV2;
import com.usexpress.myapplication.Service.CallApi;
import com.usexpress.myapplication.Service.CallApiToken;
import com.usexpress.myapplication.Service.GetSMS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class Serviced extends Service {
    public static int counter = 0;
    public static int counterSave = 0;
    private int countTime = DataSetting.TimeDelay;
    private int countSaveTime = 1;
    static final String MY_PREFS_NAME = "MyPrefsFile";
    HomeActivity homeActivity = new HomeActivity();
    private Timer timer, timerSave;
    private TimerTask timerTask;
    GetSMS SMS = new GetSMS();
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }
    public void startTimerCallApi() {
        ContentResolver cr = getContentResolver();
        timer = new Timer();
//        timerTask = new TimerTask() {
//            public void run() {
//                if (counter == DataSetting.TimeDelay) {
////                    SMS.readSMS(cr);
////                    pushSMS();
////                    counter = 0;
////                    Save();
////                    Toaster.toast("App SMS running...");
////                    timer.cancel();
////                    startTimerCallApi();
//                }
//                Log.i("Count", "=========  " + counter);
//                counter++;
//            }
//        };
//        timer.schedule(timerTask, 1000, 1000); //
    }


    void pushSMS(){
        if(DataSetting.arraySMSMain!=null && DataSetting.arraySMSMain.size() >0)
        for(int i = 0 ;i<DataSetting.arraySMSMain.size();i++){
            if(DataSetting.arraySMSMain.get(i).arrSMS!=null && DataSetting.arraySMSMain.get(i).arrSMS.size()>0){
                for(int x = 0 ;x <DataSetting.arraySMSMain.get(i).arrSMS.size();x++){
                    if(!DataSetting.arraySMSMain.get(i).arrSMS.get(x).isSucceeded()){
                        callApi(DataSetting.arraySMSMain.get(i).arrSMS.get(x).getID(),DataSetting.arraySMSMain.get(i).arrSMS.get(x).getPhone(),DataSetting.arraySMSMain.get(i).arrSMS.get(x).getMessage(),
                                DataSetting.arraySMSMain.get(i).arrSMS.get(x).getDate_SMSArrived(),i,x);
                    }
                }
            }
        }
        Save();
    }
    public void callApi(long id, String phone, String body, String dateSMS,int i,int x) {
        ApiV2.ApiService2.Push("" + DataSetting.myPhoneNumber, phone, body, dateSMS).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("--------Thành công-*------" + DataSetting.myPhoneNumber, "" + response.code());
                if (response.code() == 200) {
                    Toaster.toast("Đã gửi nội dung: ID:" + id + " [Phone: " + phone + "]");
                    DataSetting.arraySMSMain.get(i).arrSMS.get(x).setSucceeded(true);
                } else {
                    Toaster.toast("Gửi Thông tin thất bại:  ID:" + id + "  " + response.code() + " --- [Phone: " + phone + "]");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("--------Thất bại-------", "" + t);
                Toaster.toast("Gửi Thông tin thất bại: ID: " + id + "  [Phone: " + phone + "]");
                DataSetting.arraySMSMain.get(i).arrSMS.get(x).setDate_CallSuccessful("Api: ERROR");
            }
        });
    }


    void Save() {
        SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataSetting.arraySMSMain);
        prefsEditor.putString(DataSetting.KeySMS, json);
        prefsEditor.apply();
        Log.d("Save cache SMS", "----------Save cache success");
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