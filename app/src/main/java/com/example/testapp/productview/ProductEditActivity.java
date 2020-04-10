package com.example.testapp.productview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testapp.BaseActivity;
import com.example.testapp.R;

public class ProductEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
    }
}
