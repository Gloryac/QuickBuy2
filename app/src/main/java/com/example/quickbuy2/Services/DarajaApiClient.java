package com.example.quickbuy2.Services;

import static com.example.quickbuy2.Constants.CONNECT_TIMEOUT;
import static com.example.quickbuy2.Constants.READ_TIMEOUT;
import static com.example.quickbuy2.Constants.WRITE_TIMEOUT;

import android.util.Log;

import com.example.quickbuy2.Interceptor.AccessTokenInterceptor;
import com.example.quickbuy2.Interceptor.AuthInterceptor;
import com.example.quickbuy2.Model.AccessToken;
import com.example.quickbuy2.Model.STKPush;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class DarajaApiClient {
    private Retrofit retrofit;
    private boolean isDebug;
    private boolean isGetAccessToken;
    private String mAuthToken;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    private static final String BASE_URL = "https://sandbox.safaricom.co.ke/"; // Sandbox environment (replace with "api" for production)

    public DarajaApiClient() throws IOException {
        buildOkHttpClient();
    }

    public interface STKPushService {
        //@GET("oauth/v1/generate?grant_type=client_credentials")
        @GET(" https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
        Call<AccessToken> getAccessToken();

        @POST("mpesa/stkpush/v1/processrequest")
        Call<STKPush> sendPush(@Body STKPush stkPush);
    }


    public DarajaApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public DarajaApiClient setAuthToken(String authToken) {
        mAuthToken = authToken;
        Log.d("mpesa auth", "onResponse auth token: "+mAuthToken);
        return this;
    }
    public DarajaApiClient setGetAccessToken(boolean getAccessToken) {
        isGetAccessToken = getAccessToken;
        return this;
    }
    private OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(httpLoggingInterceptor);
        }
        if (mAuthToken != null && !mAuthToken.isEmpty()) {
            clientBuilder.addInterceptor(new AuthInterceptor(mAuthToken));
        }
        if(isGetAccessToken){
            clientBuilder.addInterceptor(new AccessTokenInterceptor()); // Add AccessTokenInterceptor here
        }

        OkHttpClient client = clientBuilder.build();
        return client;
    }

    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(buildOkHttpClient());
        retrofit = builder.build();
        return retrofit;
    }
    public STKPushService mpesaService() {
        return getRestAdapter().create(STKPushService.class);
    }

}