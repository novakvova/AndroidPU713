package com.example.testapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class CovidApplication extends Application {

    private static CovidApplication instance;
    private static Context appContext;
    private Activity mCurrentActivity = null;

    public static CovidApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }


    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
