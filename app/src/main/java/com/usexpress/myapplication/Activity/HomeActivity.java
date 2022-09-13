package com.usexpress.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.TokenModel;
import com.usexpress.myapplication.R;
import com.usexpress.myapplication.Restarter;
import com.usexpress.myapplication.Service.ApiV2;
import com.usexpress.myapplication.Service.GetSMS;
import com.usexpress.myapplication.Service.GetTokenService;
import com.usexpress.myapplication.Serviced;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class HomeActivity extends AppCompatActivity {

    Intent mServiceIntent;
    DataSetting data = new DataSetting();
    static String myPhoneNumber = "";
    public ListView lv;
    public Context context = HomeActivity.this;
    Button btResend, btSetting, btRefresh;
    private Serviced mYourService;
    static final String MY_PREFS_NAME = "MyPrefsFile";

    public static AdapterListView adapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        setContentView(R.layout.activity_home);
        lv = findViewById(R.id.lv);
        btResend = findViewById(R.id.btResend);
        btSetting = findViewById(R.id.btSetting);
        btRefresh = findViewById(R.id.refesh);
        onclick();
        DataSetting.arrayContentSendSMS.add("Xin cảm ơn !!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d("Check SMSM", "1111111111111111111111111111");

        } else {

            Log.d("Check SMSM", "22222222222222222222222222222222");
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            DataSetting.myPhoneNumber = tMgr.getLine1Number();
        }
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }
        mYourService = new Serviced();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
             Log.d("android.permission.READ_SMS", "true");
        }
        getCache();
    }

    private void onclick() {
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
        btResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFail(true);
            }
        });
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.toast("Đang gửi lại SMS fail");
                ContentResolver cr = getContentResolver();
                GetSMS getSMS = new GetSMS();
                getSMS.readSMS(cr);
                adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview,
                        sortList(addList()));
                lv.setAdapter(adapterListView);
                adapterListView.notifyDataSetChanged();
                Toaster.toast("Updated....");
            }
        });
    }

    boolean checkSort(long a, long b) {
        if (a < b) {
            return true;
        }
        return false;
    }

    ArrayList<ItemModel> addList(){
        ArrayList<ItemModel> list= new ArrayList<>();
        if(DataSetting.arraySMSMain!= null){
            for(int i = 0; i<DataSetting.arraySMSMain.size();i++){
                if(DataSetting.arraySMSMain.get(i).arrSMS !=null||DataSetting.arraySMSMain.get(i).arrSMS.size() >0){
                    list.addAll(DataSetting.arraySMSMain.get(i).arrSMS);
                }
            }
        }
        return list;
    }

    ArrayList<ItemModel> sortList(ArrayList<ItemModel> list) {
        ItemModel model;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int u = i + 1; u < list.size(); u++) {
                if (checkSort(list.get(i).getID(), list.get(u).getID())) {
                    model = list.get(u);
                    list.set(u, list.get(i));
                    list.set(i, model);
                }
            }
        }

        return list;
    }

    public void getCache() {
        try {
            SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Gson gson = new Gson();
            String jsonArr = sharedPref.getString(DataSetting.KeySMS, null);
            String jsonArrContent = sharedPref.getString(DataSetting.KeyContent, null);
            int jsonTime = sharedPref.getInt(DataSetting.KeyTime, 30);
            DataSetting.TimeDelay = jsonTime;
            Log.d("get cache Time delay:", ""+DataSetting.TimeDelay);
            if (jsonArr != null) {
                Type type = new TypeToken<ArrayList<MainSMS>>() {
                }.getType();
                ArrayList<MainSMS> arrayItems = gson.fromJson(jsonArr, type);
                DataSetting.arraySMSMain.clear();
                DataSetting.arraySMSMain.addAll(arrayItems);
                adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList(addList()));
                lv.setAdapter(adapterListView);
                adapterListView.notifyDataSetChanged();
                Log.d("get cache arr main :", "Length: "+DataSetting.arraySMSMain.size());
                adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview,
                        sortList(addList()));
                lv.setAdapter(adapterListView);
                adapterListView.notifyDataSetChanged();
            }
            if (jsonArrContent != null) {
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> arrayItems = gson.fromJson(jsonArrContent, type);
                DataSetting.arrayContentSendSMS.clear();
                DataSetting.arrayContentSendSMS.addAll(arrayItems);
                Log.d("get cache List content:", "Length: "+DataSetting.arrayContentSendSMS.size());
            }
        }catch (Exception e){
            Log.d("Error cache", ""+e);
        }
//        Login();
    }
    public void SendFail(boolean load) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String dateNow = formatter.format(date);
        if (DataSetting.arraySMSMain == null || DataSetting.arraySMSMain.size() == 0) {
            Toaster.toast("Không đọc được SMS nào !!");
        } else {
            for (int i = 0; i < DataSetting.arraySMSMain.size(); i++) {
                for(int y =0; y < DataSetting.arraySMSMain.get(i).arrSMS.size();y++){
                    if(!DataSetting.arraySMSMain.get(i).arrSMS.get(y).isSucceeded()){
                        callApi( DataSetting.arraySMSMain.get(i).arrSMS.get(y).getID(),
                                DataSetting.arraySMSMain.get(i).arrSMS.get(y).getPhone(),
                                DataSetting.arraySMSMain.get(i).arrSMS.get(y).getMessage(),
                                DataSetting.arraySMSMain.get(i).arrSMS.get(y).getDate_SMSArrived(), dateNow, load, DataSetting.arraySMSMain.get(i).Content,i,y);
                    }
                }
            }
        }
    }
    public void callApi(long id, String phone, String body, String dateSMS, String dateNow, boolean load,String content,int i,int y) {
        ApiV2.ApiService2.Push("" + myPhoneNumber, phone, body, dateSMS).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("--------Thành công-*------" + DataSetting.myPhoneNumber, "" + response.code());
                if (response.code() == 200) {
                    Toaster.toast("Đã gửi nội dung: ID:" + id + " [Phone: " + phone + "]");
                    DataSetting.arraySMSMain.get(i).arrSMS.get(y).setSucceeded(true);
                    DataSetting.arraySMSMain.get(i).arrSMS.get(y).setDate_CallSuccessful(""+dateNow);
                } else {
                    Toaster.toast("Gửi Thông tin thất bại:  ID:" + id + "  " + response.code() + " --- [Phone: " + phone + "]");
                    DataSetting.arraySMSMain.get(i).arrSMS.get(y).setDate_CallSuccessful(""+response.code());
                }
                if (load) {
                    adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList(addList()));
                    lv.setAdapter(adapterListView);
                    adapterListView.notifyDataSetChanged();
                }
                if(data.isAnswerTime(DataSetting.arraySMSMain.get(i).timeStartTracker,DataSetting.arraySMSMain.get(i).timeEndTracker)){
                    if(DataSetting.arraySMSMain.get(i).arrSMS.get(y).getAnswer() == 0  && DataSetting.arraySMSMain.get(i).isSendSMS){
                        DataSetting.arraySMSMain.get(i).arrSMS.get(y).setAnswer(data.AnswerAction(phone,content));
                    } else if (DataSetting.arraySMSMain.get(i).arrSMS.get(y).getAnswer() == 1) {
                    }else{
                        DataSetting.arraySMSMain.get(i).arrSMS.get(y).setAnswer(2);
                    }
                }
                return;
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("--------Thất bại-------", "" + t);
                Toaster.toast("Gửi Thông tin thất bại: ID: " + id + "  [Phone: " + phone + "]");
                DataSetting.arraySMSMain.get(i).arrSMS.get(y).setDate_CallSuccessful("Api: ERROR");
                if(data.isAnswerTime(DataSetting.arraySMSMain.get(i).timeStartTracker,DataSetting.arraySMSMain.get(i).timeEndTracker)){
                    if(DataSetting.arraySMSMain.get(i).arrSMS.get(y).getAnswer() == 0  && DataSetting.arraySMSMain.get(i).isSendSMS){
                        DataSetting.arraySMSMain.get(i).arrSMS.get(y).setAnswer(data.AnswerAction(phone,content));
                    } else if (DataSetting.arraySMSMain.get(i).arrSMS.get(y).getAnswer() == 1) {
                    } else{
                        DataSetting.arraySMSMain.get(i).arrSMS.get(y).setAnswer(2);
                    }
                }
                if (load) {
                    adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList(addList()));
                    lv.setAdapter(adapterListView);
                    adapterListView.notifyDataSetChanged();
                }
            }
        });

        if (load) {
            adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList(addList()));
            lv.setAdapter(adapterListView);
            adapterListView.notifyDataSetChanged();
        }
    }

//    void DialogS(String s) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setMessage(s).setTitle("Error Login")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //do things
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    private void GetDomainAndUrl(String s) {
//        String url = "";
//        String[] parts = s.split("/");
//        for (int i = 0; i < parts.length - 1; i++) {
//            if (i == 0) {
//                url = url + parts[i];
//            } else {
//
//                url = url + "/" + parts[i];
//            }
//        }
//        data.DoMain = url + "/";
//        data.UrlApi = parts[parts.length - 1];
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }else{
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                myPhoneNumber = tMgr.getLine1Number();
                Log.d("Check SMSM", "-------------"+myPhoneNumber);
            }
        } catch (Exception e) {



        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Running", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}