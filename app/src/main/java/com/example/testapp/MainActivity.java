package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        EditText editText = findViewById(R.id.txtName);
        TextView textView = findViewById(R.id.vtInfo);
        String name=editText.getText().toString();
        textView.setText(name);

        Toast.makeText(this,
                name,Toast.LENGTH_LONG).show();
    }
}
