package com.example.testapp;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import com.example.testapp.productview.ProductGridFragment;
import com.example.testapp.userview.UserGridFragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            String token = this.getToken();

            if (token != null && !token.isEmpty()) {
                this.currentFragment=new ProductGridFragment();
            } else {
                this.currentFragment=new LoginFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, this.currentFragment)
                    .commit();
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
                this.navigateTo(new ProductGridFragment(), false); // Navigate to the next Fragment
                //intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                return true;
            }
            case R.id.register: {
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
                //this.navigateTo(new RegisterFragment(), false);
                return true;
            }
            case R.id.login: {
                this.navigateTo(new LoginFragment(), true);
                return true;
            }
            case R.id.users:
                this.navigateTo(new UserGridFragment(), true); // Navigate to the next Fragment
                return true;
            case R.id.logout:
                removeToken();
                this.navigateTo(new LoginFragment(), false); // Navigate to the next Fragment
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }


}
