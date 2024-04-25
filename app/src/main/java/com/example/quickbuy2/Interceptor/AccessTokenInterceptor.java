package com.example.quickbuy2.Interceptor;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quickbuy2.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {
    public AccessTokenInterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic Zk9NQXJJRm9rbGtLTjVCa29xRFZSZVlUZWNLV2sweGYybXpPUEl6VDhqQWtHZENsOkdibE1QTVNGeVRaYW1EUEdYRUFLZGh6cXRwdk9GdWNkdjlyWURBSTVKemRPWjhRdloxbDg2ZWxwb0ZWczNIeFQ" )
                .build();
        Log.d("mpesa auth", "accessToken Intercept: request"+request);
        return chain.proceed(request);
    }
}