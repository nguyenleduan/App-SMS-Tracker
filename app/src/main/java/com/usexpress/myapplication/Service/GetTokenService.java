package com.usexpress.myapplication.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.TokenModel;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetTokenService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    GetTokenService ApiService = new Retrofit.Builder()
            .baseUrl("https://api.usexpressglobal.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(GetTokenService.class);
    @FormUrlEncoded
    @POST("token")
    Call<TokenModel> GetToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);
}