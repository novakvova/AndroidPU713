package com.example.testapp.user.apiUser;

import com.example.testapp.network.interceptors.AuthorizationInterceptor;
import com.example.testapp.network.interceptors.ConnectivityInterceptor;
import com.example.testapp.network.interceptors.JWTInterceptor;
import com.example.testapp.productview.api.ProductDTOHolderApi;
import com.example.testapp.productview.api.ProductDTOService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDTOService {
    private static UserDTOService mInstance;
    private static final String BASE_URL = "https://covidbackcursova.azurewebsites.net/api/"; //"http://10.0.2.2/api/"; ///
    private Retrofit mRetrofit;

    private UserDTOService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(new JWTInterceptor())
                .addInterceptor(interceptor)
                .addInterceptor(new AuthorizationInterceptor());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static UserDTOService getInstance() {
        if (mInstance == null) {
            mInstance = new UserDTOService();
        }
        return mInstance;
    }

    public UserDTOHolderApi getJSONApi() {
        return mRetrofit.create(UserDTOHolderApi.class);
    }
}