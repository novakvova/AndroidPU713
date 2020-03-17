package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = findViewById(R.id.btnGoRegister);

        // создаем обработчик нажатия
        View.OnClickListener oclBtnRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Меняем текст в TextView (tvOut)
//                Toast.makeText(MainActivity.this,
//                        "Перехід на реєстрацію",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };

        // присвоим обработчик кнопке OK (btnOk)
        btnRegister.setOnClickListener(oclBtnRegister);
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
