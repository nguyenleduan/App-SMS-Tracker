package com.usexpress.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.ResponseBody;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.SMSModel;
import com.usexpress.myapplication.Model.TokenModel;
import com.usexpress.myapplication.Model.bodyModel;
import com.usexpress.myapplication.R;
import com.usexpress.myapplication.Restarter;
import com.usexpress.myapplication.Service.ApiV2;
import com.usexpress.myapplication.Service.CallApi;
import com.usexpress.myapplication.Service.GetTokenService;
import com.usexpress.myapplication.Serviced;
import com.usexpress.myapplication.ToastHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class HomeActivity extends AppCompatActivity {

    Intent mServiceIntent;
    DataSetting data;
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
            getCache();
        }
    }

    private void onclick() {
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        btResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.toast("Đang gửi lại SMS fail");
                ContentResolver cr = getContentResolver();
                getSMS(cr, true);
            }
        });
        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview,
                        sortList());
                lv.setAdapter(adapterListView);
                adapterListView.notifyDataSetChanged();
            }
        });
    }

    boolean checkSort(long a, long b) {
        if (a < b) {
            return true;
        }
        return false;
    }

    ArrayList<ItemModel> sortList() {
        ItemModel model;
        for (int i = 0; i < DataSetting.arrayListData.size() - 1; i++) {
            for (int u = i + 1; u < DataSetting.arrayListData.size(); u++) {
                if (checkSort(DataSetting.arrayListData.get(i).getID(), DataSetting.arrayListData.get(u).getID())) {
                    model = DataSetting.arrayListData.get(u);
                    DataSetting.arrayListData.set(u, DataSetting.arrayListData.get(i));
                    DataSetting.arrayListData.set(i, model);
                }
            }
        }

        return DataSetting.arrayListData;
    }

    public void getCache() {
        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonModel = sharedPref.getString("DataKey", null);
        String jsonArr = sharedPref.getString(DataSetting.KeySMS, null);
        data.isUrlToken = sharedPref.getBoolean("UrlTokenKey", false);
        if (jsonModel != null) {
            DataModel datas = gson.fromJson(jsonModel, DataModel.class);
            data.dataProfile = datas;
            DataSetting.TimeDelay = data.dataProfile.TimeDelay;
            if (!data.dataProfile.Link.equals("")) {
                GetDomainAndUrl(data.dataProfile.Link);
            }
        }
        if (jsonArr != null) {
            Type type = new TypeToken<ArrayList<ItemModel>>() {
            }.getType();
            ArrayList<ItemModel> arrayItems = gson.fromJson(jsonArr, type);
            DataSetting.arrayListData.clear();
            DataSetting.arrayListData.addAll(arrayItems);
            adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList());
            lv.setAdapter(adapterListView);
            adapterListView.notifyDataSetChanged();
        }
        Log.d("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        ContentResolver cr = getContentResolver();
        getSMS(cr, true);
//        Login();
    }

    public boolean checkDataCache() {
        if (DataSetting.dataProfile == null
                || DataSetting.dataProfile.AllowPhone == null) {
            return false;
        }
        return true;
    }

    public void getSMS(ContentResolver cr, boolean load) {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
//        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
        DataSetting.arrayListSMS.clear();
        if (!cursor.moveToFirst()) {
            return;
        }
        try {
            final int idIndex = cursor.getColumnIndex("_id");
            final int nameIndex = cursor.getColumnIndex("address");
            final int descriptionIndex = cursor.getColumnIndex("body");
            final int dateSMS = cursor.getColumnIndex("date");
            //TODO ??
            if (!cursor.moveToFirst()) {
            }
            DataSetting.arrayListSMS.clear();
            //
            do {
                final long id = cursor.getLong(idIndex);
                final String phone = cursor.getString(nameIndex);
                final String body = cursor.getString(descriptionIndex);
                Time time = new Time();
                time.setToNow();
                final String date = DateFormat.format("HH:mm:ss dd/MM/yyyy", new Date(cursor.getLong(dateSMS))).toString();
//                Log.d("TIME TEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + phone, Long.toString(time.toMillis(false)));
                /// TODO check phone add list
//                DataSetting.dataProfile.AllowPhone = "teckcombank,111";
                if (checkDataCache()) {
                    String[] list = DataSetting.dataProfile.AllowPhone.split(",");
                    Log.d("Check list zise:", list.length + "");
                    for (int i = 0; i < list.length; i++) {
                        if (phone.contains(list[i]) && cursor.getLong(dateSMS) > DataSetting.dataProfile.DateStart) {
                            DataSetting.arrayListSMS.add(new SMSModel(id, phone, body, date));
                            //TODO check
                            Log.d("phone:", phone);
                            Log.d("body", body);
                            Log.d("date", date);
                            Log.d("list length", DataSetting.arrayListSMS.size() + "");
                            Log.d("-------------------------", "-------------------------");
                        }
                    }
                }
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
        checkSMS(load);
    }

    public void checkSMS(boolean load) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String dateNow = formatter.format(date);
        if (DataSetting.arrayListSMS == null || DataSetting.arrayListSMS.size() == 0) {
            Toaster.toast("Không đọc được SMS nào !!");
        } else {
            for (int i = 0; i < DataSetting.arrayListSMS.size(); i++) {
                if (checkNew(DataSetting.arrayListSMS.get(i).getID())) {
                    callApi(DataSetting.arrayListSMS.get(i).getID(),
                            DataSetting.arrayListSMS.get(i).getPhone(),
                            DataSetting.arrayListSMS.get(i).getBody(),
                            DataSetting.arrayListSMS.get(i).getDate(), dateNow, load);
                }
            }
            checkFail(load);
        }
    }

    public boolean checkNew(long id) {
        if (DataSetting.arrayListData == null || DataSetting.arrayListData.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < DataSetting.arrayListData.size(); i++) {
                if (DataSetting.arrayListData.get(i).getID() == id) {
                    return false;
                }
            }
            return true;
        }
    }

    public void checkFail(boolean load) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateNow = formatter.format(date);
        for (int i = 0; i < DataSetting.arrayListData.size(); i++) {
            if (!DataSetting.arrayListData.get(i).isSucceeded()) {
                callApi(DataSetting.arrayListData.get(i).getID(),
                        DataSetting.arrayListData.get(i).getPhone(),
                        DataSetting.arrayListData.get(i).getMessage(),
                        DataSetting.arrayListData.get(i).getDate_SMSArrived(),
                        dateNow, load);
            }
        }
    }

    public void checkSaveCache(ItemModel model) {
        if (DataSetting.arrayListData.size() == 0) {
            data.arrayListData.add(model);
        } else {
            if (checkNew(model.getID())) {
                data.arrayListData.add(model);
            } else {
                for (int i = 0; i < DataSetting.arrayListData.size(); i++) {
                    if (DataSetting.arrayListData.get(i).getID() == model.getID()) {
                        data.arrayListData.get(i).setSucceeded(model.isSucceeded());
                    }
                }
            }
        }
    }

    public void callApi(long id, String phone, String body, String dateSMS, String dateNow, boolean load) {

        ApiV2.ApiService2.Push("" + myPhoneNumber, phone, body, dateSMS).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("--------Thành công-*------" + myPhoneNumber, "" + response.code());
                if (response.code() == 200) {
                    checkSaveCache(new ItemModel(id, phone, body, dateSMS, dateNow, true));
                    Toaster.toast("Đã gửi nội dung: ID:" + id + " [Phone: " + phone + "]");
                } else {
                    checkSaveCache(new ItemModel(id, phone, body, dateSMS, "Gửi Api fail " + response.code(), false));
                    Toaster.toast("Gửi Thông tin thất bại:  ID:" + id + "  " + response.code() + " --- [Phone: " + phone + "]");
                }
                if (load) {
                    adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList());
                    lv.setAdapter(adapterListView);
                    adapterListView.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("--------Thất bại-------", "" + t);
                checkSaveCache(new ItemModel(id, phone, body, dateSMS, "Error Api", false));
                Toaster.toast("Gửi Thông tin thất bại: ID: " + id + "  [Phone: " + phone + "]");
                if (load) {
                    adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList());
                    lv.setAdapter(adapterListView);
                    adapterListView.notifyDataSetChanged();
                }
            }
        });

        if (load) {
            adapterListView = new AdapterListView(HomeActivity.this, R.layout.item_listfail_listview, sortList());
            lv.setAdapter(adapterListView);
            adapterListView.notifyDataSetChanged();
        }
    }

    private void Login() {
        if (!data.isUrlToken) {
            if (data.dataProfile != null) {
                if (data.dataProfile.User != null && data.dataProfile.Pass != null) {
                    try {
                        GetTokenService.ApiService.GetToken(data.dataProfile.User, data.dataProfile.Pass, "password").enqueue(new Callback<TokenModel>() {
                            @Override
                            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                                if (response.code() == 200) {
                                    data.Token = response.body().access_token;
                                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(HomeActivity.this, "Đã đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<TokenModel> call, Throwable t) {
                                Toast.makeText(HomeActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Lỗi home activity 114", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    void DialogS(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage(s).setTitle("Error Login")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void GetDomainAndUrl(String s) {
        String url = "";
        String[] parts = s.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if (i == 0) {
                url = url + parts[i];
            } else {

                url = url + "/" + parts[i];
            }
        }
        data.DoMain = url + "/";
        data.UrlApi = parts[parts.length - 1];
    }

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