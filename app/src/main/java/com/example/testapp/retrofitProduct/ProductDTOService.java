package com.example.testapp.retrofitProduct;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.testapp.LoginFragment;
import com.example.testapp.NavigationHost;
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



        Interceptor interJWT = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                CovidApplication context = (CovidApplication)CovidApplication.getAppContext();
                JwtServiceHolder jwtService = (JwtServiceHolder)context.getCurrentActivity();

                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer "+ jwtService.getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        };

        Interceptor intetAuth = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .build();

                Response response =  chain.proceed(newRequest);
                //Log.d("MyApp", "Code : "+response.code());

                if (response.code() == 401){
                    CovidApplication context = (CovidApplication)CovidApplication.getAppContext();
                    NavigationHost navigationHost = (NavigationHost)context.getCurrentActivity();
                    navigationHost.navigateTo(new LoginFragment(), false);
                    return response;
                }

                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(intetAuth)
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
