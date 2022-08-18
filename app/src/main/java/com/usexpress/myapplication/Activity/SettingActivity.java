package com.usexpress.myapplication.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.TokenModel;
import com.usexpress.myapplication.R;
import com.usexpress.myapplication.Service.GetTokenService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    MultiAutoCompleteTextView edtLink,edtAllPhone;
    EditText edtUser,edtPass,edtDateStart,edtTimeDelay;
    Button btSave;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DataSetting data;
    static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        edtDateStart = findViewById(R.id.editDateStart);
        edtTimeDelay = findViewById(R.id.edtTimeDelay);
        btSave = findViewById(R.id.btSave);
        edtLink = findViewById(R.id.edtLink);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtAllPhone = findViewById(R.id.edtAllPhone);
        edtAllPhone.setText(DataSetting.dataProfile.AllowPhone);
        getData();
        even();
    }

    private void even() {
        edtLink.setText("https://srv.usexpress.vn/api/app/sms/receive");
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtLink.getText().toString().equals("")) {
                    Toast.makeText(SettingActivity.this, "Link is not have data", Toast.LENGTH_SHORT).show();
                } else if (edtAllPhone.getText().toString().equals("")) {
                    Toast.makeText(SettingActivity.this, "AllowPhone is not have data", Toast.LENGTH_SHORT).show();
                } else {
                    Save();
                    CallApi();
                    Toast.makeText(SettingActivity.this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void Save() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        DataModel mData = new DataModel();
        Date d = null;
        try {
            d = f.parse(edtDateStart.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = d.getTime();
        mData.DateStart =milliseconds;
        mData.TimeDelay = Integer.parseInt(edtTimeDelay.getText().toString());
        DataSetting.dataProfile.TimeDelay = Integer.parseInt(edtTimeDelay.getText().toString());
        mData.User = edtUser.getText().toString();
        mData.Pass = edtPass.getText().toString();
        mData.Link = edtLink.getText().toString();
        if(mData.User.equals("")){
            data.isUrlToken = true;
        }
        mData.AllowPhone = edtAllPhone.getText().toString();
        data.dataProfile = mData;
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mData);
        prefsEditor.putString("DataKey", json);
        prefsEditor.putBoolean("UrlTokenKey", data.isUrlToken);
        prefsEditor.apply();
        getData();
    }

    void getData() {
        SharedPreferences sharedPref = SettingActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String jsonModel = sharedPref.getString("DataKey", "");
        if(!jsonModel.equals("")){
            if(data.dataProfile.User.equals("")){
                data.isUrlToken = sharedPref.getBoolean("UrlTokenKey", true);
            }
            Gson gson = new Gson();
            DataModel datas = gson.fromJson(jsonModel, DataModel.class);
            data.dataProfile = datas;
            GetDomainAndUrl(data.dataProfile.Link);
            edtTimeDelay.setText(DataSetting.dataProfile.TimeDelay+"");
            edtDateStart.setText( DateFormat.format("dd/MM/yyyy", new Date(data.dataProfile.DateStart)).toString());
            edtLink.setText(DataSetting.dataProfile.Link);
            edtUser.setText(DataSetting.dataProfile.User);
            edtPass.setText(DataSetting.dataProfile.Pass);
            edtAllPhone.setText(DataSetting.dataProfile.AllowPhone);
            DataSetting.TimeDelay = DataSetting.dataProfile.TimeDelay;
            setText();
//            CallApi();
        }

    }

    private void GetDomainAndUrl(String s) {
        String url = "";
        String[] parts = s.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if(i==0){
                url = url + parts[i];
            }else{

                url = url +"/"+ parts[i];
            }
        }
        data.DoMain = url+"/";
        data.UrlApi = parts[parts.length-1];
    }
    void setText() {
        if(data.dataProfile.AllowPhone==null || data.dataProfile.AllowPhone.equals("")){
            edtAllPhone.setText("Test");
        }else{
            edtAllPhone.setText(data.dataProfile.AllowPhone);
        }

        if(data.dataProfile.Pass == null || data.dataProfile.Pass.equals("")){
            edtPass.setText("9xCx34SssP7em7VYY7SgwVXSpBqeY8");
        }else{
            edtPass.setText(data.dataProfile.Pass);
        }
        edtUser.setText(data.dataProfile.User);
        if(data.dataProfile.Link!=null){
            edtLink.setText(data.dataProfile.Link);
        }
    }


    void CallApi() {
        if (!data.dataProfile.User.equals("")&&!data.dataProfile.Pass.equals("")) {
            try {
                GetTokenService.ApiService.GetToken(data.dataProfile.User, data.dataProfile.Pass, "password").enqueue(new Callback<TokenModel>() {
                    @Override
                    public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                        if(response.code()==200){
                            DataSetting.Token = response.body().access_token;
                            data.isUrlToken=false;
                            SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            prefsEditor.putString("KeyToken", DataSetting.Token);
                            prefsEditor.apply();
                            DialogS("Đăng nhập thành công");
                        }else{
                            DialogS("Đăng nhập thất bại");
                        }
                    }
                    @Override
                    public void onFailure(Call<TokenModel> call, Throwable t) {
                        DialogS("Đăng nhập thất bại");
                    }
                });
            } catch (Exception e) {
                DialogS("Lỗi đăng nhập 3312");
            }
        } else {
            data.isUrlToken = true;
            Toast.makeText(SettingActivity.this, "Sử dụng Api chứ Token", Toast.LENGTH_SHORT).show();
        }
    }

    void DialogS(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage(s).setTitle("Thông báo")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}