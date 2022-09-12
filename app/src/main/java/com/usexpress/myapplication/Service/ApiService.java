package com.usexpress.myapplication.Service;

import android.util.Log;

import com.usexpress.myapplication.DataSetting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class ApiService {





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

}
