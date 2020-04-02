package com.example.testapp.network.interceptors;

import com.example.testapp.LoginFragment;
import com.example.testapp.NavigationHost;
import com.example.testapp.account.JwtServiceHolder;
import com.example.testapp.application.CovidApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .build();
        Response response = chain.proceed(newRequest);

        if (response.code() == 401) {
            CovidApplication context = (CovidApplication) CovidApplication.getAppContext();
            NavigationHost navigationHost = (NavigationHost) context.getCurrentActivity();
            navigationHost.navigateTo(new LoginFragment(), false);
          //  return response;
        }
        return response;
    }
}
