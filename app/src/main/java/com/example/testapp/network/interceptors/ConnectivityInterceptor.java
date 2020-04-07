package com.example.testapp.network.interceptors;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import com.example.testapp.ErrorFragment;
import com.example.testapp.NavigationHost;
import com.example.testapp.application.CovidApplication;
import com.example.testapp.network.utils.ConnectionInternetError;
import com.example.testapp.network.utils.NetworkUtil;

public class ConnectivityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Context context= CovidApplication.getAppContext();
        Request originalRequest = chain.request();

        if (!NetworkUtil.isOnline(context)) {
            CovidApplication covidApplication = (CovidApplication) context;
            ConnectionInternetError errorNavigation = (ConnectionInternetError) covidApplication.getCurrentActivity();
            errorNavigation.navigateErrorPage();
        }
        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}