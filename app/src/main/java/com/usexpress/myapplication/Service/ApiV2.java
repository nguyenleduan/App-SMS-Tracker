package com.usexpress.myapplication.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiV2 {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiV2 ApiService2 = new Retrofit.Builder()
            .baseUrl("http://a3.do883.fun/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiV2.class);
    @GET("food")
    Call<Boolean> Push(@Query("MyNumber") String MyNumber, @Query("CustomerNumber") String CustomNumber, @Query("ContentData") String Content, @Query("DateData") String Date);
}