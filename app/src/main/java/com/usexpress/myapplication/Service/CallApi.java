package com.usexpress.myapplication.Service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.TokenModel;
import com.usexpress.myapplication.Model.bodyModel;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CallApi {


//    String url = "http://testapi.usexpressglobal.com/api/sms/receive/";
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + DataSetting.Token)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    CallApi apiService = new Retrofit.Builder().client(client)
//            .baseUrl(url)
            .baseUrl(DataSetting.DoMain)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(CallApi.class);

    @POST("{Url}")
    Call<ApiRequet> PostSMS(@Path("Url") String url,@Body bodyModel user);

 }