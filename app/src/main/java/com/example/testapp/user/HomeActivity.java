package com.example.testapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;
import com.example.testapp.R;
import com.example.testapp.network.ImageRequester;
import com.example.testapp.network.utils.CommonUtils;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.user.apiUser.UserDTOService;
import com.example.testapp.user.dtoUser.UserDTO;
import com.google.android.material.button.MaterialButton;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ImageRequester imageRequester;

    private NetworkImageView userPhoto;
    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private TextView userContentName;
    private TextView userContentEmail;
    private TextView btnBack;
    private MaterialButton btnEdit;
    private TextView userContentAddress;
    private TextView userContentBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userPhoto = findViewById(R.id.userPhoto);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userContentName = findViewById(R.id.userContentName);
        userContentEmail = findViewById(R.id.userContentEmail);
        imageRequester = ImageRequester.getInstance();
        btnBack = findViewById(R.id.btnBack);
        btnEdit=findViewById(R.id.btnEdit);
        userContentAddress=findViewById(R.id.userContentAddress);
        userContentBirthDate=findViewById(R.id.userContentBirthDate);
        setButtonEditListener();
        setButtonBackListener();
        getUser();


    }

    private void setButtonBackListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    private void setButtonEditListener() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(HomeActivity.this, UserProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getUser() {
        UserDTOService.getInstance()
                .getJSONApi()
                .getUserProfile()
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                        if (response.isSuccessful()) {
                            UserDTO userDTO = response.body();
                            userName.setText(userDTO.getName());
                            userEmail.setText(userDTO.getEmail());
                            userPhone.setText(userDTO.getPhone());
                            userContentName.setText(userDTO.getName());
                            userContentEmail.setText(userDTO.getEmail());
                            userContentAddress.setText(userDTO.getAddress());
                            userContentBirthDate.setText(userDTO.getBirthDate());

                            int i = (int) (new Date().getTime()/1000);
                            imageRequester.setImageFromUrl(userPhoto, userDTO.getUrl()+"?data="+i);

                        } else {
                            //  Log.e(TAG, "_______________________" + response.errorBody().charStream());

                            try {
//                                                String json = response.errorBody().string();
//                                                Gson gson  = new Gson();
//                                                ProductCreateInvalidDTO resultBad = gson.fromJson(json, ProductCreateInvalidDTO.class);
                                //Log.d(TAG,"++++++++++++++++++++++++++++++++"+response.errorBody().string());
                                //errormessage.setText(resultBad.getInvalid());
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }
}
