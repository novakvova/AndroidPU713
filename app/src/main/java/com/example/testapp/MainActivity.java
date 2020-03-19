package com.example.testapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements NavigationHost{

    private static final String TAG = "MyActivity";
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "--------------------onCreate--------------------");
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "----------MainActivity: onStart()-------------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "---------------MainActivity: onResume()---------------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "----------------MainActivity: onPause()----------------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "-------------MainActivity: onStop()-----------------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "---------------MainActivity: onDestroy()-------------------");
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
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            }
            case R.id.login: {
                this.navigateTo(new LoginFragment(), false);
            }
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
}
