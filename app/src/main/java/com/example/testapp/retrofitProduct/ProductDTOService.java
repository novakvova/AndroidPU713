package com.example.testapp.retrofitProduct;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.account.JwtServiceHolder;
import com.example.testapp.utilsintrnet.ConnectivityInterceptor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.testapp.application.CovidApplication;

import java.io.IOException;


public class ProductDTOService {
    private static ProductDTOService mInstance;
    private static final String BASE_URL = "https://covidandroid.azurewebsites.net/api/";
    private Retrofit mRetrofit;

    private ProductDTOService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CovidApplication myApp=(CovidApplication)CovidApplication.getAppContext();
        final String token= "Bearer "+((JwtServiceHolder)myApp.getCurrentActivity()).getToken();

        Interceptor interJWT = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", token)
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interJWT)
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static ProductDTOService getInstance() {
        if (mInstance == null) {
            mInstance = new ProductDTOService();
        }
        return mInstance;
    }

    public ProductDTOHolderApi getJSONApi() {
        return mRetrofit.create(ProductDTOHolderApi.class);
    }
}
