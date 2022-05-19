package com.example.visualphysics10.net;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppForNet extends Application {
    public static ApiService api;
    private final String ROOT_URL = "https://easyfizika.ru";

    @Override
    public void onCreate() {
        super.onCreate();
        api = createRetrofit().create(ApiService.class);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getClient())
                .build();
    }


    private Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.build();
    }
}

