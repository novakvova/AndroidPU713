package com.example.testapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

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


public class MainActivity extends AppCompatActivity {

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


        //btnRegister = findViewById(R.id.btnGoRegister);

        // создаем обработчик нажатия
//        View.OnClickListener oclBtnRegister = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Меняем текст в TextView (tvOut)
////                Toast.makeText(MainActivity.this,
////                        "Перехід на реєстрацію",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        };
//
//        // присвоим обработчик кнопке OK (btnOk)
//        btnRegister.setOnClickListener(oclBtnRegister);
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

    public void onButtonClick(View view) {

        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Title")
                .setMessage("Your message goes here. Keep it short but clear.")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
//        EditText editText = findViewById(R.id.txtName);
//        TextView textView = findViewById(R.id.vtInfo);
//        String name=editText.getText().toString();
//        textView.setText(name);
//
//        Toast.makeText(this,
//                name,Toast.LENGTH_LONG).show();
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
            default:
            return super.onOptionsItemSelected(item);
        }
    }
}
