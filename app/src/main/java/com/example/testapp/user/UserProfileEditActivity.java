package com.example.testapp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.NetworkImageView;
import com.example.testapp.R;
import com.example.testapp.network.ImageRequester;
import com.example.testapp.network.utils.CommonUtils;
import com.example.testapp.network.utils.FileUtils;
import com.example.testapp.productview.api.ProductDTOService;
import com.example.testapp.user.apiUser.UserDTOService;
import com.example.testapp.user.dtoUser.UserDTO;
import com.example.testapp.user.dtoUser.UserEditDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileEditActivity extends AppCompatActivity {
    public static final int PICKFILE_RESULT_CODE = 1;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextBirthDate;
    private EditText editTextPhone;
    private EditText editTextAddress;


    private Button buttonEdit;
    private Button buttonCancel;
    private Button btnSelectImage;
    //image
    private ImageRequester imageRequester;
    private NetworkImageView editImage;
    private String chooseImageBase64;

    private UserEditDTO userEditDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        imageRequester = ImageRequester.getInstance();
        setupViews();
        initUser();
        setbtnSelectImageListener();
        setButtonEditListener();
    }

    private void setupViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextBirthDate = findViewById(R.id.editBirthDate);
        editTextPhone = findViewById(R.id.editPhone);
        editTextAddress = findViewById(R.id.editAddress);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonCancel = findViewById(R.id.buttonCancel);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        editImage = findViewById(R.id.chooseImage);
    }

    private void initUser() {
        UserDTOService.getInstance()
                .getJSONApi()
                .getEditUser()
                .enqueue(new Callback<UserEditDTO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserEditDTO> call, @NonNull Response<UserEditDTO> response) {
                        if (response.isSuccessful()) {
                            userEditDTO = response.body();
                            editTextFirstName.setText(userEditDTO.getFirstname());
                            editTextLastName.setText(userEditDTO.getLastname());
                            editTextPhone.setText(userEditDTO.getPhone());
                            editTextAddress.setText(userEditDTO.getAddress());
                            editTextEmail.setText(userEditDTO.getEmail());
                            editTextBirthDate.setText(userEditDTO.getBirthDate());
                            imageRequester.setImageFromUrl(editImage, userEditDTO.getUrl());

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
                    public void onFailure(@NonNull Call<UserEditDTO> call, @NonNull Throwable t) {
                        Log.e("ERROR", "*************ERORR request***********");
                        t.printStackTrace();

                    }
                });
    }

    private void setButtonEditListener() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = editTextFirstName.getText().toString().trim();
                String lastname = editTextLastName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String birthdate = editTextBirthDate.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String base64 = chooseImageBase64;
                userEditDTO.setAddress(address);
                userEditDTO.setFirstname(firstname);
                userEditDTO.setLastname(lastname);
                userEditDTO.setBirthDate(birthdate);
                userEditDTO.setPhone(phone);
                userEditDTO.setEmail(email);
                userEditDTO.setImageBase64(base64);


                UserDTOService.getInstance()
                        .getJSONApi()
                        .editUser(userEditDTO)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                                CommonUtils.hideLoading();
                                if (response.isSuccessful()) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                //CommonUtils.hideLoading();
                                Log.e("ERROR", "*************ERORR request***********");
                                t.printStackTrace();
                                CommonUtils.hideLoading();
                            }
                        });


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtils.from(getApplicationContext(), fileUri);
                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        editImage.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    private void setbtnSelectImageListener() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });
    }


}
