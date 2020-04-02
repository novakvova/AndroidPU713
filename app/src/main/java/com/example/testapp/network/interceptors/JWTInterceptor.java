package com.example.testapp.network.interceptors;

import com.example.testapp.LoginFragment;
import com.example.testapp.NavigationHost;
import com.example.testapp.account.JwtServiceHolder;
import com.example.testapp.application.CovidApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JWTInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        CovidApplication context = (CovidApplication)CovidApplication.getAppContext();
        JwtServiceHolder jwtService = (JwtServiceHolder)context.getCurrentActivity();
        String token = jwtService.getToken();
        if(token != null && !token.isEmpty())
        {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer "+ token)
                    .build();
            return chain.proceed(newRequest);
        }

        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}
