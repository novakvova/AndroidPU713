package com.example.testapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.testapp.account.JwtServiceHolder;
import com.example.testapp.application.CovidApplication;
import com.example.testapp.network.ProductEntry;
import com.example.testapp.productview.ProductGridFragment;
import com.example.testapp.retrofitProduct.ProductDTO;
import com.example.testapp.retrofitProduct.ProductDTOService;
import com.example.testapp.userview.UserGridFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationHost, JwtServiceHolder {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CovidApplication myApp=(CovidApplication)CovidApplication.getAppContext();
        myApp.setCurrentActivity(this);

        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            String token = this.getToken();
            if (token != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new ProductGridFragment())
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home: {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            case R.id.register: {
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
                this.navigateTo(new RegisterFragment(), false);
                return true;
            }
            case R.id.login: {
                this.navigateTo(new LoginFragment(), false);
                return true;
            }
            case R.id.users:
                this.navigateTo(new UserGridFragment(), false); // Navigate to the next Fragment
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
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
    public void SaveJWTToken(String token) {
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
}
