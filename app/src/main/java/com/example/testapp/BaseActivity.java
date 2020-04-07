package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.testapp.account.JwtServiceHolder;
import com.example.testapp.application.CovidApplication;
import com.example.testapp.network.utils.ConnectionInternetError;

public abstract class BaseActivity extends AppCompatActivity implements NavigationHost,
        JwtServiceHolder, ConnectionInternetError {

    protected Fragment currentFragment;
    private Fragment lastPageFragment;

    public BaseActivity() {
        CovidApplication myApp=(CovidApplication)CovidApplication.getAppContext();
        myApp.setCurrentActivity(this);
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        this.currentFragment = fragment;
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void saveJWTToken(String token) {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=this.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.putString("token",token);
            Log.i("Login",token);
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getToken() {
        SharedPreferences prefs=this.getSharedPreferences("jwtStore",Context.MODE_PRIVATE);
        String token = prefs.getString("token","");
        return token;
    }

    @Override
    public void removeToken() {
        SharedPreferences prefs;
        SharedPreferences.Editor edit;
        prefs=this.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        edit=prefs.edit();
        try {
            edit.remove("token");
            edit.apply();
            edit.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void navigateErrorPage() {
        this.lastPageFragment=currentFragment;
        this.navigateTo(new ErrorFragment(), false);
    }

    @Override
    public void refreshLastPage() {
        this.navigateTo(this.lastPageFragment, false);
    }
}
