package com.usexpress.myapplication.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ApiRequet;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CallApiToken {

    //    String url = "http://testapi.usexpressglobal.com/api/sms/receive/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    CallApiToken apiService = new Retrofit.Builder()
            .baseUrl(DataSetting.DoMain)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(CallApiToken.class);
    @POST("{Url}")
    Call<ApiRequet> PostSMSToken(@Path("Url") String url, @Query("Number") String phoneNumber, @Query("Message") String messenger, @Query("Time") String date );

}
